package com.openclassrooms.realestatemanager.map

import com.openclassrooms.realestatemanager.R
import android.location.Address
import android.location.Geocoder
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException
import java.util.Locale


class EstateMapActivity : AppCompatActivity(), OnMapReadyCallback {
    private var mMap: GoogleMap? = null
    private var geocoder: Geocoder? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)
        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment!!.getMapAsync(this)
        geocoder = Geocoder(this, Locale.getDefault())
    }

    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        val address = intent.getStringExtra("ESTATE_ADDRESS") ?: "Default Address"
        try {
            val addressList: List<Address>? = geocoder!!.getFromLocationName(address, 1)
            if (!addressList.isNullOrEmpty()) {
                val returnedAddress: Address = addressList[0]
                val latLng = LatLng(returnedAddress.latitude, returnedAddress.longitude)
                mMap!!.addMarker(
                    MarkerOptions().position(latLng).title("estate emplacement")
                )
                mMap!!.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
            } else {
                Toast.makeText(this, "Adresse non trouvée", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
