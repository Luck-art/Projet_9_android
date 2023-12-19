package com.openclassrooms.realestatemanager.map

import com.google.android.gms.maps.model.LatLngBounds

data class MinMaxBounds(
    val lowLat: Double,
    val lowLng: Double,
    val highLat: Double,
    val highLng: Double,
)

// from https://stackoverflow.com/questions/17735812/how-can-i-get-the-visible-markers-in-google-maps-v2-in-android

fun LatLngBounds.getMinMaxBounds(): MinMaxBounds {
    val lowLat: Double
    val lowLng: Double
    val highLat: Double
    val highLng: Double

    if (this.northeast.latitude < this.southwest.latitude) {
        lowLat = this.northeast.latitude
        highLat = this.southwest.latitude
    } else {
        highLat = this.northeast.latitude
        lowLat = this.southwest.latitude
    }
    if (this.northeast.longitude < this.southwest.longitude) {
        lowLng = this.northeast.longitude
        highLng = this.southwest.longitude
    } else {
        highLng = this.northeast.longitude
        lowLng = this.southwest.longitude
    }

    return MinMaxBounds(
        lowLat = lowLat,
        lowLng = lowLng,
        highLat = highLat,
        highLng = highLng,
    )
}