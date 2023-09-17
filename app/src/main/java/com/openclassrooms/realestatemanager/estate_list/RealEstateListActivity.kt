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

        /*setupDrawer(
            drawerLayoutId = R.id.drawer_layout,
            navigationViewId = R.id.nav_view,
            burgerMenuId = R.id.burger_menu
        )*/

    }
}
