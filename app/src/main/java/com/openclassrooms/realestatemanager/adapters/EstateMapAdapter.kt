package com.openclassrooms.realestatemanager.adapters

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import java.io.IOException

class EstateMapAdapter(private val context: Context, private val lat: Double, private val lng: Double, private val geocoder: Geocoder) : OnMapReadyCallback {

    private var mMap: GoogleMap? = null
    override fun onMapReady(googleMap: GoogleMap) {
        mMap = googleMap
        try {
            val addressList: List<Address>? = geocoder.getFromLocation(lat, lng, 1)
            if (!addressList.isNullOrEmpty()) {
                val returnedAddress: Address = addressList[0]
                mMap?.let { map ->
                    val latLng = LatLng(returnedAddress.latitude, returnedAddress.longitude)
                    map.addMarker(MarkerOptions().position(latLng).title("Emplacement de la propriété"))
                    map.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15f))
                }

            } else {
                Toast.makeText(context, "Adresse non trouvée", Toast.LENGTH_SHORT).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }
}
