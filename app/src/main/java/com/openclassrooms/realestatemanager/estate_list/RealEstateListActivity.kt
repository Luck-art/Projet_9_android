package com.openclassrooms.realestatemanager.estate_list

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.RealEstateListAdapter
import com.openclassrooms.realestatemanager.models.RealEstateListModel
import com.openclassrooms.realestatemanager.service.GooglePlacesApi
import com.openclassrooms.realestatemanager.utils.setupDrawer
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RealEstateListActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.real_estate_list_activity)

        recyclerView = findViewById(R.id.recyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        fetchEstatesFromApi()

        setupDrawer(
            drawerLayoutId = R.id.drawer_layout,
            navigationViewId = R.id.nav_view,
            burgerMenuId = R.id.burger_menu
        )

    }

    private fun fetchEstatesFromApi() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://maps.googleapis.com/maps/api/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        val api = retrofit.create(GooglePlacesApi::class.java)

        val latitude = ""
        val longitude = ""
        val radius = 0
        val placeType = ""
        val apiKey = ""

        api.getPlaces("$latitude,$longitude", radius, placeType, apiKey)
            .enqueue(object : Callback<RealEstateListModel> {
                override fun onResponse(
                    call: Call<RealEstateListModel>,
                    response: Response<RealEstateListModel>
                ) {
                    val estatesFromApi = response.body()
                    recyclerView.adapter = RealEstateListAdapter(estatesFromApi)
                }

                override fun onFailure(call: Call<RealEstateListModel>, t: Throwable) {

                }
            })
    }
}
