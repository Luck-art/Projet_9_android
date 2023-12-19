package com.openclassrooms.realestatemanager.map

import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.location.Location
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.lifecycleScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.android.ext.android.inject

class AroundMeMapActivity : AppCompatActivity() {

    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private var map: GoogleMap? = null
    private val realEstateDao: RealEstateDao by inject()

    companion object {
        const val PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 1
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_map)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)

        val mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment?
        mapFragment?.getMapAsync { map ->
            this.map = map
            askPermission()
        }
    }

    private fun askPermission() {
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.ACCESS_FINE_LOCATION
            )
            != PackageManager.PERMISSION_GRANTED
        ) {
            ActivityCompat.requestPermissions(
                this, arrayOf(android.Manifest.permission.ACCESS_FINE_LOCATION),
                PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION
            )
        } else {
            fetchUserPosition()
        }
    }

    @SuppressLint("MissingPermission") // asked before
    private fun fetchUserPosition() {
        fusedLocationClient.lastLocation
            .addOnSuccessListener { location: Location? ->
                if (location != null) {
                    // center map around me
                    map?.moveCamera(
                        CameraUpdateFactory.newLatLngZoom(
                            LatLng(
                                location.latitude,
                                location.longitude
                            ), 15f
                        )
                    )
                    fetchPlaces()
                }
            }
    }

    private fun fetchPlaces() {
        map?.setOnCameraMoveListener {
            val cameraVisibleBounds =
                map?.projection?.visibleRegion?.latLngBounds ?: return@setOnCameraMoveListener

            lifecycleScope.launch {
                val nearbyEstates = withContext(Dispatchers.IO) {
                    val bounds = cameraVisibleBounds.getMinMaxBounds()
                    realEstateDao.getRealEstatesInRadius(
                        latMin = bounds.lowLat,
                        latMax = bounds.highLat,
                        lngMin = bounds.lowLng,
                        lngMax = bounds.highLng,
                    )
                }

                map?.let { m ->
                    nearbyEstates.forEach { estate ->
                        val estateLat = estate.latitude
                        val estateLng = estate.longitude
                        if (estateLat != null && estateLng != null) {
                            m.addMarker(
                                MarkerOptions()
                                    .position(
                                        LatLng(estateLat, estateLng)
                                    )
                                    .title(estate.name)
                            )
                        }
                    }
                }
            }
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION -> {
                if ((grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
                    fetchUserPosition()
                } else {
                    Toast.makeText(
                        this@AroundMeMapActivity,
                        "permission required to use this screen",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                return
            }

        }
    }


}
