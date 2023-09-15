package com.openclassrooms.realestatemanager.service

import com.openclassrooms.realestatemanager.models.RealEstateListModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface GooglePlacesApi {
    @GET("")
    fun getPlaces(
        @Query("location") location: String,
        @Query("radius") radius: Int,
        @Query("type") type: String,
        @Query("key") apiKey: String
    ): Call<RealEstateListModel>
}
