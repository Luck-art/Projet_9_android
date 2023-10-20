package com.openclassrooms.realestatemanager.details

import EstateDetailsAdapter
import android.annotation.SuppressLint
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.MediaController
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import android.widget.VideoView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.RealEstateManagerAdapter
import com.openclassrooms.realestatemanager.models.EstateDetailsModel
import com.openclassrooms.realestatemanager.view_models.EstateDetailsViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class EstateDetailsActivity : AppCompatActivity() {

    val callViewModel : EstateDetailsViewModel by viewModel()

    lateinit var playButton: ImageButton
    lateinit var pauseButton: ImageButton

    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.estate_detail)
        val textView = findViewById<TextView>(R.id.estate_name)
        val descriptionTextView = findViewById<TextView>(R.id.estate_description)
        val priceTextView = findViewById<TextView>(R.id.estate_price)
        val imageView = findViewById<ImageView>(R.id.estate_imageview)
        val imagesRecyclerView = findViewById<RecyclerView>(R.id.image_list)
        imagesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)

        playButton = findViewById(R.id.playButton)
        pauseButton = findViewById(R.id.pauseButton)

        lifecycleScope.launchWhenStarted {
            callViewModel.state.collect { viewState ->
                viewState?.realEstate?.let {
                    textView.text = it.name
                    descriptionTextView.text = it.description
                    priceTextView.text = it.price.toString()
                    Glide.with(this@EstateDetailsActivity)
                        .load(it.img)
                        .into(imageView)

                    val adapter = EstateDetailsAdapter(mediaItems = viewState.medias) { mediaItem ->
                        if (mediaItem.type == "video") {
                            findViewById<ImageView>(R.id.main_display_image).visibility = View.GONE
                            val videoView = findViewById<VideoView>(R.id.main_display_video)
                            videoView.visibility = View.VISIBLE
                            val videoUri = Uri.parse(mediaItem.uri)
                            videoView.setVideoURI(videoUri)
                            videoView.start()

                            playButton.visibility = View.VISIBLE
                            pauseButton.visibility = View.GONE
                        } else {
                            findViewById<VideoView>(R.id.main_display_video).visibility = View.GONE
                            val imageView = findViewById<ImageView>(R.id.main_display_image)
                            imageView.visibility = View.VISIBLE
                            Glide.with(this@EstateDetailsActivity)
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





        val videoView = findViewById<VideoView>(R.id.main_display_video)




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


    }
}
