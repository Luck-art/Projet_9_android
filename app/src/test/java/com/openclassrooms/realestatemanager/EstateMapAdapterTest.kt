package com.openclassrooms.realestatemanager

import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.widget.Toast
import com.google.android.gms.maps.CameraUpdate
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.Marker
import com.google.android.gms.maps.model.MarkerOptions
import com.google.common.base.CharMatcher.any
import com.google.common.base.Verify.verify
import com.openclassrooms.realestatemanager.adapters.EstateMapAdapter
import org.junit.Before
import org.junit.Test
import org.mockito.kotlin.any
import org.mockito.kotlin.mock
import org.mockito.kotlin.verify
import org.mockito.kotlin.whenever
import java.io.IOException
import java.util.Locale


class EstateMapAdapterTest {

    private lateinit var adapter: EstateMapAdapter
    private val context = mock<Context>()
    private val googleMap = mock<GoogleMap>()
    private val geocoder = mock<Geocoder>()

    @Before
    fun setUp() {
        adapter = EstateMapAdapter(context, 0.0, 0.0)
    }

    @Test
    fun onMapReady_withValidAddress_addsMarkerAndMovesCamera() {
        val address = Address(Locale.getDefault()).apply {
            latitude = 0.0
            longitude = 0.0
        }
        whenever(geocoder.getFromLocation(any<Double>(), any<Double>(), any<Int>())).thenReturn(listOf(address))


        adapter.onMapReady(googleMap)

        verify(googleMap).addMarker(any<MarkerOptions>())
        verify(googleMap).moveCamera(any<CameraUpdate>())
    }

    @Test
    fun onMapReady_withNoAddress_showsToast() {
        whenever(geocoder.getFromLocation(any<Double>(), any<Double>(), any<Int>())).thenReturn(emptyList())

        adapter.onMapReady(googleMap)

        fun Context.showToast(message: CharSequence, duration: Int = Toast.LENGTH_SHORT) {
            Toast.makeText(this, message, duration).show()
        }

    }

    @Test(expected = IOException::class)
    fun onMapReady_withIOException_handlesException() {
        whenever(geocoder.getFromLocation(any<Double>(), any<Double>(), any<Int>())).thenThrow(IOException())

        adapter.onMapReady(googleMap) }

}
