package com.openclassrooms.realestatemanager.estate_manager

import RealEstateManagerViewModel
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.EditText
import android.widget.ImageView
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.RealEstateManagerAdapter
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.details.EstateDetailsActivity
import com.openclassrooms.realestatemanager.estate_manager.logic.AddNewEstate
import com.openclassrooms.realestatemanager.estate_manager.logic.SearchFilter
import com.openclassrooms.realestatemanager.models.DialogState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import org.koin.androidx.viewmodel.ext.android.viewModel



class RealEstateManagerActivity : AppCompatActivity() {

    private lateinit var addButton: ImageView
    val callViewModel : RealEstateManagerViewModel by viewModel()
    private lateinit var realEstateRecyclerView: RecyclerView
    lateinit var addNewEstate: AddNewEstate
    private var isInEditMode = false

    private lateinit var adapter: RealEstateManagerAdapter







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
        val viewModel = ViewModelProvider(this).get(RealEstateManagerViewModel::class.java)

        lifecycleScope.launch {
            val names = callViewModel.getAllRealEstateNames().first()
            val builder = AlertDialog.Builder(this@RealEstateManagerActivity)
            builder.setTitle("Choisir un bien immobilier pour la modification")
            builder.setItems(names.toTypedArray()) { dialog, which ->
                val selectedName = names[which]
                val selectedRealEstate = viewModel.getRealEstateByName(selectedName)
                lifecycleScope.launch {
                    selectedRealEstate.collect { realEstate ->
                        if (realEstate != null) {
                            addNewEstate.showAddPropertyDialog(viewModel, this@RealEstateManagerActivity, realEstate)
                        }
                    }
                }

            }
            builder.setNegativeButton("Annuler", null)
            builder.show()
        }
    }






    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.real_estate_manager)

        supportActionBar?.hide()


        val searchIcon: ImageView = findViewById(R.id.search_estate)
        val searchEditText: EditText = findViewById(R.id.searchEditText)
        val searchFilter by lazy { SearchFilter(adapter) }
        val addButton: ImageView = findViewById(R.id.add_estate)
        val editButton: ImageView = findViewById(R.id.edit_estate)
        val database = Room.databaseBuilder(
            this,
            RealEstateManagerDatabase::class.java,
            "real_estate_manager_database"
        ).build()

        realEstateRecyclerView = findViewById(R.id.realEstateRecyclerView)
        realEstateRecyclerView.layoutManager = LinearLayoutManager(this)

        val realEstateDao = database.realEstateDao()

        addNewEstate = AddNewEstate()



        fun onEstateItemClicked(realEstate: RealEstate, isInEditMode: Boolean) {
            if (isInEditMode) {
                callViewModel.selectRealEstateForEditing(realEstate)
            } else {
                val intent = Intent(this, EstateDetailsActivity::class.java)
                intent.putExtra("estate_id", realEstate.id)
                startActivity(intent)
            }
        }






        //  RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.realEstateRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        lifecycleScope.launchWhenStarted {
            callViewModel.viewState.collect { list ->
                adapter = RealEstateManagerAdapter(list, ::onEstateItemClicked)
                recyclerView.adapter = adapter
            }
        }

        addButton.setOnClickListener {
            callViewModel.onAddButtonClicked()
        }

        editButton.setOnClickListener {
            showSelectEstateDialog()
        }



        searchIcon.setOnClickListener {
            Log.d("SearchIcon", "Icon clicked")
            searchFilter.toggleSearchBar(searchEditText)
        }

        callViewModel.showDialog.observe(this) { shouldShow ->
            when(shouldShow) {
                DialogState.creation -> {
                    addNewEstate.showAddPropertyDialog(context = this@RealEstateManagerActivity, viewModel = callViewModel, realEstate = null)
                }
                is DialogState.edit -> {
                    addNewEstate.showAddPropertyDialog(context = this@RealEstateManagerActivity, viewModel = callViewModel, realEstate = shouldShow.estate)
                }
                null -> {}
            }
        }

        callViewModel.selectedRealEstate.observe(this) { selectedEstate ->
            if (selectedEstate != null) {

            }
        }

    }


    private fun updateEditModeInAdapter() {
        (realEstateRecyclerView.adapter as? RealEstateManagerAdapter)?.setEditMode(isInEditMode)
    }


}

