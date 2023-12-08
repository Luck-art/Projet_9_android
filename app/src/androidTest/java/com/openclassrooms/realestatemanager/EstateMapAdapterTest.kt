package com.openclassrooms.realestatemanager

import android.content.Context
import android.location.Address
import android.location.Geocoder
import com.google.android.gms.maps.GoogleMap
import com.google.common.base.CharMatcher.any
import com.google.common.base.Verify.verify
import com.openclassrooms.realestatemanager.adapters.EstateMapAdapter
import org.junit.Before
import org.junit.Test
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
        whenever(geocoder.getFromLocation(any(), any(), any())).thenReturn(listOf(address))

        adapter.onMapReady(googleMap)

        verify(googleMap).addMarker(any())
        verify(googleMap).moveCamera(any())
    }

    @Test
    fun onMapReady_withNoAddress_showsToast() {
        whenever(geocoder.getFromLocation(any(), any(), any())).thenReturn(emptyList())

        adapter.onMapReady(googleMap)

        verify(context).showToast(any())
    }

    @Test(expected = IOException::class)
    fun onMapReady_withIOException_handlesException() {
        whenever(geocoder.getFromLocation(any(), any(), any())).thenThrow(IOException())

        adapter.onMapReady(googleMap) }

}
