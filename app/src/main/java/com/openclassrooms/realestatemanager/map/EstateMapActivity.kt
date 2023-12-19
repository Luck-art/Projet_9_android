package com.openclassrooms.realestatemanager.map

import android.content.pm.PackageManager
import com.openclassrooms.realestatemanager.R
import android.location.Geocoder
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.LatLngBounds
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

    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        if (ContextCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION)
            != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION)
        } else {

        }

        val lat = intent.getDoubleExtra("ESTATE_LAT", 0.0)
        val lng = intent.getDoubleExtra("ESTATE_LNG", 0.0)
        geocoder = Geocoder(this, Locale.getDefault())

        estateMapAdapter = EstateMapAdapter(this, lat, lng, geocoder)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(estateMapAdapter)

        val dao = RealEstateManagerDatabase.getInstance(this).realEstateDao()

        lifecycleScope.launch {
            val nearbyEstates = withContext(Dispatchers.IO) {
                val latMin = lat - 0.01
                val latMax = lat + 0.01
                val lngMin = lng - 0.01
                val lngMax = lng + 0.01
                dao.getRealEstatesInRadius(latMin, latMax, lngMin, lngMax)
            }

            val builder = LatLngBounds.Builder()
            for (estate in nearbyEstates) {
                val estateLat = estate.latitude ?: 0.0
                val estateLng = estate.longitude ?: 0.0
                val estateName = estate.name ?: ""
                val latLng = LatLng(estateLat, estateLng)
                builder.include(latLng)
                estateMapAdapter.addMarker(estateLat, estateLng, estateName)
            }
            if (nearbyEstates.isNotEmpty()) {
                val bounds = builder.build()
                estateMapAdapter.adjustCameraToBounds(bounds)
                Log.d("EstateMapActivity", "Nearby Estates: $nearbyEstates")

            }

        }

    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {

                }
                return
            }

        }
    }


}
