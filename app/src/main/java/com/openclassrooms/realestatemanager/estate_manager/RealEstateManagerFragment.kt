package com.openclassrooms.realestatemanager.estate_manager

import RealEstateManagerViewModel
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.EditText
import android.widget.RadioGroup
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.RealEstateManagerAdapter
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.estate_manager.logic.AddNewEstate
import com.openclassrooms.realestatemanager.estate_manager.logic.SpacesItemDecoration
import com.openclassrooms.realestatemanager.models.DialogState
import com.openclassrooms.realestatemanager.utils.NetworkStateListener
import com.openclassrooms.realestatemanager.utils.Utils
import kotlinx.coroutines.CompletableDeferred
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel


class RealEstateManagerFragment : Fragment() {

    interface Listener {
        fun onItemClicked(item: RealEstate)
    }

    val callViewModel : RealEstateManagerViewModel by viewModel()
    private lateinit var realEstateRecyclerView: RecyclerView
    lateinit var addNewEstate: AddNewEstate
    private var isInEditMode = false

    private lateinit var adapter: RealEstateManagerAdapter

      var checkBoxHouse: CheckBox? = null
      var checkBoxLoft: CheckBox? = null
      var checkBoxApartment: CheckBox? = null
    private fun onDeleteEstateClicked(estateId: Long) {
        callViewModel.onDeleteEstateClicked(estateId)
    }

    var pickMediaCompletable : CompletableDeferred<Uri?>? = null
    val pickMedia = registerForActivityResult(ActivityResultContracts.PickVisualMedia()) { uri ->

        if (uri != null) {
            Log.d("PhotoPicker", "Selected URI: $uri")
        } else {
            Log.d("PhotoPicker", "No media selected")
        }

        pickMediaCompletable?.complete(uri)
    }

    fun onFilterNewItems(realEstates: List<RealEstate>) {
        adapter.onNewItems(realEstates)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return when (item.itemId) {
            R.id.edit_estate -> {
                isInEditMode = !isInEditMode
                updateEditModeInAdapter()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun showSelectEstateDialog() {
        lifecycleScope.launch {
            val names = callViewModel.getAllRealEstateNames().first()
            val builder = AlertDialog.Builder(requireContext())
            builder.setTitle("Choisir un bien immobilier pour la modification")
            builder.setItems(names.toTypedArray()) { dialog, which ->
                val selectedName = names[which]
                val selectedRealEstate = callViewModel.getRealEstateByName(selectedName)
                lifecycleScope.launch {
                    selectedRealEstate.collect { realEstate ->
                        if (realEstate != null) {
                            addNewEstate.showAddPropertyDialog(
                                callViewModel,
                                requireContext(),
                                realEstate,
                                pickPhoto = {
                                    val completable = CompletableDeferred<Uri?>()
                                    pickMediaCompletable = completable

                                    pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                                    completable.await()
                                },
                            )
                            val estateTypes = realEstate.estate_type.split(", ")
                            checkBoxHouse = requireView().findViewById(R.id.checkBoxHouse)
                            checkBoxLoft = requireView().findViewById(R.id.checkBoxLoft)
                            checkBoxApartment = requireView().findViewById(R.id.checkBoxApartment)
                        }
                    }
                }

            }
            builder.setNegativeButton("Annuler", null)
            builder.show()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val rootView = inflater.inflate(R.layout.real_estate_manager, container, false)

        Utils.checkNetworkAvailability(requireContext(), object : NetworkStateListener {
            override fun onNetworkAvailable() {
            }

            override fun onNetworkUnavailable() {
                showNetworkUnavailableMessage(rootView)
            }
        })

        return rootView
    }

    private fun showNetworkUnavailableMessage(rootView: View) {
        val snackbar = Snackbar.make(rootView, "Le réseau n'est pas disponible", Snackbar.LENGTH_INDEFINITE)
        snackbar.setAction("Réessayer") {
        }
        snackbar.show()
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        realEstateRecyclerView = view.findViewById(R.id.realEstateRecyclerView)
        realEstateRecyclerView.layoutManager = LinearLayoutManager(requireContext())

        addNewEstate = AddNewEstate()

        fun onEstateItemClicked(realEstate: RealEstate, isInEditMode: Boolean) {
            if (isInEditMode) {
                callViewModel.selectRealEstateForEditing(realEstate)
            } else {
                (activity as? Listener)?.let {
                    it.onItemClicked(realEstate)
                }
            }
        }


        val spaceInPixels = TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP,
            8f,
            resources.displayMetrics
        ).toInt()

        realEstateRecyclerView.addItemDecoration(SpacesItemDecoration(spaceInPixels))



        //  RecyclerView
        val recyclerView: RecyclerView = view.findViewById(R.id.realEstateRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        lifecycleScope.launchWhenStarted {
            callViewModel.viewState.collect { list ->
                adapter = RealEstateManagerAdapter(
                    dataSet = list,
                    items = list,
                    onItemClicked = ::onEstateItemClicked,
                    isInEditMode = isInEditMode

                ) { estateId -> onDeleteEstateClicked(estateId) }
                recyclerView.adapter = adapter
            }
        }


        callViewModel.showDialog.observe(viewLifecycleOwner) { shouldShow ->
            when(shouldShow) {
                DialogState.creation -> {
                    addNewEstate.showAddPropertyDialog(
                        context = requireContext(),
                        viewModel = callViewModel,
                        realEstate = null,
                        pickPhoto = {
                            val completable = CompletableDeferred<Uri?>()
                            pickMediaCompletable = completable

                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                            completable.await()
                        }
                    )
                }
                is DialogState.edit -> {
                    addNewEstate.showAddPropertyDialog(
                        context = requireContext(),
                        viewModel = callViewModel,
                        realEstate = shouldShow.estate,
                        pickPhoto = {
                            val completable = CompletableDeferred<Uri?>()
                            pickMediaCompletable = completable

                            pickMedia.launch(PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageOnly))

                            completable.await()
                        }
                    )
                }
                null -> {}
            }
        }

        callViewModel.selectedRealEstate.observe(viewLifecycleOwner) { selectedEstate ->
            if (selectedEstate != null) {

            }
        }


    }

    private fun updateEditModeInAdapter() {
        (realEstateRecyclerView.adapter as? RealEstateManagerAdapter)?.setEditMode(isInEditMode)
    }

    fun onFilterTextChanged(text: String) {
        adapter.filterItems(text)
    }

    fun onAddButtonClicked() {
        callViewModel.onAddButtonClicked()
    }

    fun toggleEditState() {
        isInEditMode = !isInEditMode
        updateEditModeInAdapter()
    }
}