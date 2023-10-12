package com.openclassrooms.realestatemanager.details

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
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.EstateDetailsAdapter

class EstateDetailsActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {



        super.onCreate(savedInstanceState)
        setContentView(R.layout.estate_detail)

        val estateName = intent.getStringExtra("estateName")
        val textView = findViewById<TextView>(R.id.estate_name)
        textView.text = estateName

        val estateDescription = intent.getStringExtra("estateDescription")
        val descriptionTextView = findViewById<TextView>(R.id.estate_description)
        descriptionTextView.text = estateDescription

        val estatePrice = intent.getStringExtra("estatePrice")
        val priceTextView = findViewById<TextView>(R.id.estate_price)
        priceTextView.text = estatePrice

        val estateImageUrl = intent.getStringExtra("estateImageUrl")
        val imageView = findViewById<ImageView>(R.id.estate_imageview)

        Glide.with(this)
            .load(estateImageUrl)
            .into(imageView)

        val videoView = findViewById<VideoView>(R.id.estate_video)

        val videoUri = Uri.parse("https://player.vimeo.com/external/539011265.sd.mp4?s=ac4e448f51f5a518ea1b971f79fc7a9986f2a359&profile_id=164&oauth2_token_id=57447761")

        videoView.setVideoURI(videoUri)

        val mediaController = MediaController(this)
        videoView.setMediaController(mediaController)
        mediaController.setAnchorView(videoView)

        videoView.setOnCompletionListener {
            videoView.start()
        }

        val playButton: ImageButton = findViewById(R.id.playButton)
        val pauseButton: ImageButton = findViewById(R.id.pauseButton)

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




        val imagesRecyclerView = findViewById<RecyclerView>(R.id.image_list)

        val estateImageUrls = listOf(
            "https://images.pexels.com/photos/126271/pexels-photo-126271.jpeg?auto=compress&cs=tinysrgb&w=1600",
            "https://images.pexels.com/photos/276554/pexels-photo-276554.jpeg?auto=compress&cs=tinysrgb&w=1600",
            "https://images.pexels.com/photos/5563472/pexels-photo-5563472.jpeg?auto=compress&cs=tinysrgb&w=1600",
            "https://images.pexels.com/photos/1486785/pexels-photo-1486785.jpeg?auto=compress&cs=tinysrgb&w=1600"
        )

        val adapter = EstateDetailsAdapter(imageUrls = estateImageUrls)
        imagesRecyclerView.layoutManager = LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        imagesRecyclerView.adapter = adapter



    }
}
