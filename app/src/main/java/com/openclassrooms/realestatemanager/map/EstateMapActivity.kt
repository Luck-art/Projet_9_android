package com.openclassrooms.realestatemanager.map

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R

/**
 * Map only to focus on the estate
 */
class EstateMapActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        val lat = intent.getDoubleExtra("ESTATE_LAT", 0.0)
        val lng = intent.getDoubleExtra("ESTATE_LNG", 0.0)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(object : OnMapReadyCallback {
            override fun onMapReady(map: GoogleMap) {
                val estatePlace = LatLng(lat, lng)
                map.addMarker(
                    MarkerOptions().position(
                        estatePlace
                    ).title("Emplacement de la propriété")
                )

                map.moveCamera(CameraUpdateFactory.newLatLngZoom(estatePlace, 15f))
            }
        })
    }

}
