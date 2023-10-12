package com.openclassrooms.realestatemanager.details

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R

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

    }
}
