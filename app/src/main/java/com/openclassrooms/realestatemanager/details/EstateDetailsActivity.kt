package com.openclassrooms.realestatemanager.details

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import android.widget.TextView
import com.openclassrooms.realestatemanager.R

class EstateDetailsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.estate_detail)

        val estateName = intent.getStringExtra("estateName")
        val textView = findViewById<TextView>(R.id.estate_name_textview)
        textView.text = estateName
    }
}
