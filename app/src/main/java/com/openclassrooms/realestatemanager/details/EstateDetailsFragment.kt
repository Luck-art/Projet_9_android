package com.openclassrooms.realestatemanager.details

import EstateDetailsAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.VideoView
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.RealEstateManagerAdapter
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.map.EstateMapActivity
import com.openclassrooms.realestatemanager.models.EstateDetailsModel
import com.openclassrooms.realestatemanager.view_models.EstateDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateDetailsFragment : Fragment() {

    val callViewModel : EstateDetailsViewModel by viewModel()

    lateinit var playButton: ImageButton
    lateinit var pauseButton: ImageButton

    companion object {
        fun newInstance(estate_id: Long): EstateDetailsFragment{
            val args = Bundle()
            args.putLong("estate_id", estate_id)
            val fragment = EstateDetailsFragment()
            fragment.arguments = args
            return fragment
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.estate_detail, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val textView = view.findViewById<TextView>(R.id.estate_name)
        val descriptionTextView = view.findViewById<TextView>(R.id.estate_description)
        val priceTextView = view.findViewById<TextView>(R.id.estate_price)
        val imageView = view.findViewById<ImageView>(R.id.estate_imageview)
        val imagesRecyclerView = view.findViewById<RecyclerView>(R.id.image_list)
        val statusTextView: TextView = view.findViewById(R.id.estate_status)
        Log.d("DEBUG", "Status TextView: $statusTextView")


        imagesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        playButton = view.findViewById(R.id.playButton)
        pauseButton = view.findViewById(R.id.pauseButton)

        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        callViewModel.state.collect { viewState ->
                viewState?.let { vs ->
                    vs.realEstate?.let {
                        textView.text = it.name
                        descriptionTextView.text = it.description
                        priceTextView.text = it.price.toString()
                        Glide.with(this@EstateDetailsFragment)
                            .load(it.img)
                            .into(imageView)

                        vs.realEstate?.let { estate ->
                            if (estate.sended) {
                                statusTextView.text = "À vendre"
                            } else {
                                Log.d("DEBUG", "Status TextView: $statusTextView")

                                statusTextView.text = "Vendu"
                            }
                        }
                    }


                    val estateValue = vs.realEstate
                    if (estateValue != null) {
                        val currentEstate: RealEstate = estateValue

                        val adapter = EstateDetailsAdapter(currentEstate = currentEstate, mediaItems = vs.medias) { mediaItem ->
                            if (mediaItem.type == "video") {
                                view.findViewById<ImageView>(R.id.main_display_image).visibility = View.GONE
                                val videoView = view.findViewById<VideoView>(R.id.main_display_video)
                                videoView.visibility = View.VISIBLE
                                val videoUri = Uri.parse(mediaItem.uri)
                                videoView.setVideoURI(videoUri)
                                videoView.start()

                                playButton.visibility = View.VISIBLE
                                pauseButton.visibility = View.GONE
                            } else {
                                view.findViewById<VideoView>(R.id.main_display_video).visibility = View.GONE
                                val imageView = view.findViewById<ImageView>(R.id.main_display_image)
                                imageView.visibility = View.VISIBLE
                                Glide.with(this@EstateDetailsFragment)
                                    .load(mediaItem.uri)
                                    .into(imageView)

                                playButton.visibility = View.GONE
                                pauseButton.visibility = View.GONE
                            }
                        }
                        imagesRecyclerView.adapter = adapter

                    } else {

                    }
                }
            }
        }






        val videoView = view.findViewById<VideoView>(R.id.main_display_video)




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
            val estate = callViewModel.state.value?.realEstate
            if (estate != null) {
                intent.putExtra("ESTATE_LAT", estate.latitude)
            }
            if (estate != null) {
                intent.putExtra("ESTATE_LNG", estate.longitude)
            }
            startActivity(intent)
        }




    }
}
