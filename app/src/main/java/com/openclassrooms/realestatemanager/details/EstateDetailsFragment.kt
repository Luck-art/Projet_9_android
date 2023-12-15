package com.openclassrooms.realestatemanager.details

import android.annotation.SuppressLint
import android.content.Intent
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.TextView
import android.widget.VideoView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.EstateDetailsAdapter
import com.openclassrooms.realestatemanager.adapters.EstateDetailsAdapter.Companion.REQUEST_CODE_PICK_MEDIA
import com.openclassrooms.realestatemanager.database.tables.Media
import com.openclassrooms.realestatemanager.map.EstateMapActivity
import com.openclassrooms.realestatemanager.view_models.EstateDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateDetailsFragment : Fragment() {

    private val callViewModel: EstateDetailsViewModel by viewModel()
    private lateinit var playButton: ImageButton
    private lateinit var pauseButton: ImageButton
    private lateinit var imagesRecyclerView: RecyclerView
    private val adapter = EstateDetailsAdapter(mutableListOf())

    private val getContent = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        uri?.let {
            Log.d("EstateDetailsFragment", "URI sélectionnée: $uri")
            val estateId = arguments?.getLong("estate_id") ?: 0L
            val newMedia = Media(
                uri = uri.toString(),
                description = "",
                realEstateId = estateId,
                type = "image"
            )
            adapter.addMediaItem(newMedia)
            callViewModel.insertMedia(newMedia)
        } ?: run {
            Log.d("EstateDetailsFragment", "Aucun URI reçu")
        }
    }

    companion object {
        fun newInstance(estate_id: Long): EstateDetailsFragment {
            val args = Bundle()
            args.putLong("estate_id", estate_id)
            val fragment = EstateDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.estate_detail, container, false)
    }

    @SuppressLint("CutPasteId")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val textView = view.findViewById<TextView>(R.id.estate_name)
        val descriptionTextView = view.findViewById<TextView>(R.id.estate_description)
        val priceTextView = view.findViewById<TextView>(R.id.estate_price)
        val imageView = view.findViewById<ImageView>(R.id.estate_imageview)
        //val statusTextView = view.findViewById<TextView>(R.id.estate_status)
        val videoView = view.findViewById<VideoView>(R.id.main_display_video)

        val addButton: ImageView? = view.findViewById(R.id.add_button)
        addButton?.setOnClickListener {
            openGallery()
        }

        imagesRecyclerView = view.findViewById<RecyclerView>(R.id.image_list)
        imagesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        imagesRecyclerView.adapter = adapter


        playButton = view.findViewById(R.id.playButton)
        pauseButton = view.findViewById(R.id.pauseButton)

        adapter.setOnItemClickedListener { mediaItem ->
            val imageViewToBeUpdated = view.findViewById<ImageView>(R.id.main_display_image)
            val videoViewToBeUpdated = view.findViewById<VideoView>(R.id.main_display_video)

            val playButton = view.findViewById<ImageButton>(R.id.playButton)
            val pauseButton = view.findViewById<ImageButton>(R.id.pauseButton)

            when (mediaItem.type) {
                "video" -> {
                    imageViewToBeUpdated.visibility = View.GONE
                    videoViewToBeUpdated.visibility = View.VISIBLE
                    videoViewToBeUpdated.setVideoURI(Uri.parse(mediaItem.uri))
                    videoViewToBeUpdated.start()

                    playButton.visibility = View.VISIBLE
                    pauseButton.visibility = View.GONE
                }
                "image" -> {
                    videoViewToBeUpdated.visibility = View.GONE
                    imageViewToBeUpdated.visibility = View.VISIBLE
                    Glide.with(this).load(mediaItem.uri).into(imageViewToBeUpdated)

                    playButton.visibility = View.GONE
                    pauseButton.visibility = View.GONE
                }
            }
        }



        adapter.setOnAddButtonClickedListener {
            openGallery()
        }

        val surfaceTextView = view.findViewById<TextView>(R.id.estate_surface)
        val addressTextView = view.findViewById<TextView>(R.id.estate_address)
        val roomsTextView = view.findViewById<TextView>(R.id.estate_rooms)

        val priceFormatter: NumberFormat = NumberFormat.getCurrencyInstance().apply {
            maximumFractionDigits = 0
            currency = Currency.getInstance("EUR")
        }

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            callViewModel.state?.collect { viewState ->
                viewState?.let { vs ->
                    textView.text = vs.realEstate?.name
                    descriptionTextView.text = vs.realEstate?.description
                    priceTextView.text = vs.realEstate?.price?.let { priceFormatter.format(it) }
                    Glide.with(this@EstateDetailsFragment).load(vs.realEstate?.img).into(imageView)
                    adapter.updateMediaItems(vs.medias)
                    surfaceTextView.text = "Surface: ${vs.realEstate?.surface} m²"
                    addressTextView.text = "Adresse: ${vs.realEstate?.address}"
                    roomsTextView.text = "Rooms: ${vs.realEstate?.rooms}"
                    Log.d("EstateDetailsFragment", "Mise à jour des éléments de l'adapter")
                }
            }
        }

        playButton.setOnClickListener {
            videoView.start()
            playButton.visibility = View.GONE
            pauseButton.visibility = View.VISIBLE
        }

        pauseButton.setOnClickListener {
            videoView.pause()
            playButton.visibility = View.VISIBLE
            pauseButton.visibility = View.GONE
        }

        val mapButton: ImageButton = view.findViewById(R.id.btn_map)
        mapButton.setOnClickListener {
            val intent = Intent(requireContext(), EstateMapActivity::class.java)
            callViewModel.state?.value?.realEstate?.let { estate ->
                intent.putExtra("ESTATE_LAT", estate.latitude)
                intent.putExtra("ESTATE_LNG", estate.longitude)
            }
            startActivity(intent)
        }
    }

    private fun openGallery() {
        val intent = Intent(Intent.ACTION_PICK)
        getContent.launch("*/*")
        intent.putExtra(Intent.EXTRA_MIME_TYPES, arrayOf("image/*", "video/*"))
        startActivityForResult(intent, REQUEST_CODE_PICK_MEDIA)
    }


}
