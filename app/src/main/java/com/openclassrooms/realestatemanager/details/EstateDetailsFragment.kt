package com.openclassrooms.realestatemanager.details

import EstateDetailsAdapter
import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import android.os.Bundle
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
import com.openclassrooms.realestatemanager.map.EstateMapActivity
import com.openclassrooms.realestatemanager.models.EstateDetailsModel
import com.openclassrooms.realestatemanager.view_models.EstateDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateDetailsFragment : Fragment() {

    val callViewModel : EstateDetailsViewModel by viewModel()

    lateinit var playButton: ImageButton
    lateinit var pauseButton: ImageButton

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
        imagesRecyclerView.layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

        playButton = view.findViewById(R.id.playButton)
        pauseButton = view.findViewById(R.id.pauseButton)

        lifecycleScope.launchWhenStarted {
            callViewModel.state.collect { viewState ->
                viewState?.realEstate?.let {
                    textView.text = it.name
                    descriptionTextView.text = it.description
                    priceTextView.text = it.price.toString()
                    Glide.with(this@EstateDetailsFragment)
                        .load(it.img)
                        .into(imageView)

                    val adapter = EstateDetailsAdapter(mediaItems = viewState.medias) { mediaItem ->
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
            // Ne pas oublier de rajouter l'adresse
            startActivity(intent)
        }



    }
}
