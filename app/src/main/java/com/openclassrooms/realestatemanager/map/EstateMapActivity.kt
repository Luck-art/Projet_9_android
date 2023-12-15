package com.openclassrooms.realestatemanager.map

import com.openclassrooms.realestatemanager.R
import android.location.Geocoder
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.SupportMapFragment
import com.openclassrooms.realestatemanager.adapters.EstateMapAdapter
import java.util.Locale


class EstateMapActivity : AppCompatActivity() {

    lateinit var geocoder: Geocoder
    private lateinit var estateMapAdapter: EstateMapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val lat = intent.getDoubleExtra("ESTATE_LAT", 0.0)
        val lng = intent.getDoubleExtra("ESTATE_LNG", 0.0)
        geocoder = Geocoder(this, Locale.getDefault())

        estateMapAdapter = EstateMapAdapter(this, lat, lng, geocoder)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(estateMapAdapter)
    }
}

