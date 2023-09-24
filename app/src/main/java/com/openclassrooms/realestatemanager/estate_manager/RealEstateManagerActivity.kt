package com.openclassrooms.realestatemanager.estate_manager

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.RealEstateManagerAdapter
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import com.openclassrooms.realestatemanager.estate_manager.logic.AddNewEstate
import com.openclassrooms.realestatemanager.models.RealEstateManagerModel
import com.openclassrooms.realestatemanager.view_models.RealEstateManagerViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.koin.androidx.viewmodel.ext.android.viewModel


class RealEstateManagerActivity : AppCompatActivity() {



    private  lateinit var  addButton: ImageView
    val callViewModel : RealEstateManagerViewModel by viewModel()
    //val addNewEstate = AddNewEstate(this, realEstateDao)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.real_estate_manager)


       //addButton = findViewById(R.id.add_estate)

        //  RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.realEstateRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)


        lifecycleScope.launchWhenStarted {
            callViewModel.viewState.collect {
                val adapter = RealEstateManagerAdapter(it)

                    recyclerView.adapter = adapter
            }
        }

        /*addButton.setOnClickListener {
           // addNewEstate.showAddPropertyDialog()
        }*/
    }


}
