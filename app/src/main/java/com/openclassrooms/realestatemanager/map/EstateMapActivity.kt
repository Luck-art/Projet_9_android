package com.openclassrooms.realestatemanager.map

import com.openclassrooms.realestatemanager.R
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.SupportMapFragment
import com.openclassrooms.realestatemanager.adapters.EstateMapAdapter
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

        val dao = RealEstateManagerDatabase.getInstance(this).realEstateDao()

        lifecycleScope.launch {
            val nearbyEstates = withContext(Dispatchers.IO) {
                val radiusInMeters = 8000
                val latMin = lat - (radiusInMeters / 111000.0)
                val latMax = lat + (radiusInMeters / 111000.0)
                val lngMin = lng - (radiusInMeters / (111000.0 * Math.cos(Math.toRadians(lat))))
                val lngMax = lng + (radiusInMeters / (111000.0 * Math.cos(Math.toRadians(lat))))
                Log.d("EstateMapActivity", "latMin: $latMin, latMax: $latMax, lngMin: $lngMin, lngMax: $lngMax")
                dao.getRealEstatesInRadius(latMin, latMax, lngMin, lngMax)
            }

            for (estate in nearbyEstates) {
                val estateLat = estate.latitude ?: 0.0
                val estateLng = estate.longitude ?: 0.0
                val estateName = estate.name ?: ""
                Log.d("EstateMapActivity", "Estate Name: $estateName, Lat: $estateLat, Lng: $estateLng")
                estateMapAdapter.addMarker(estateLat, estateLng, estateName)
            }
        }
    }
}
