package com.openclassrooms.realestatemanager.estate_manager

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.room.Room
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.adapters.RealEstateManagerAdapter
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import com.openclassrooms.realestatemanager.models.RealEstateManagerModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RealEstateManagerActivity : AppCompatActivity() {

    private lateinit var realEstateDao: RealEstateDao
    private lateinit var imageDao: ImagesDao
    private lateinit var sellerNameDao: SellerNameDao

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.real_estate_manager)

        val db = Room.databaseBuilder(
            applicationContext,
            RealEstateManagerDatabase::class.java, "database-name"
        ).build()

        realEstateDao = db.realEstateDao()
        imageDao = db.imageDao()
        sellerNameDao = db.sellerNameDao()

        CoroutineScope(Dispatchers.IO).launch {
            initializeDatabase(realEstateDao, imageDao, sellerNameDao)
        }

        // Initialize RecyclerView
        val recyclerView: RecyclerView = findViewById(R.id.realEstateRecyclerView)
        recyclerView.layoutManager = LinearLayoutManager(this)

        CoroutineScope(Dispatchers.IO).launch {
            initializeDatabase(realEstateDao, imageDao, sellerNameDao)

            val realEstates = realEstateDao.getAll()

            val adapter = RealEstateManagerAdapter(realEstates)

            withContext(Dispatchers.Main) {
                recyclerView.adapter = adapter
            }
        }
    }
    private suspend fun initializeDatabase(realEstateDao: RealEstateDao, imageDao: ImagesDao, sellerNameDao: SellerNameDao) {
        if (realEstateDao.getRowCount() == 0) {
            RealEstateManagerModel.getDefaultSellers().forEach { seller ->
                sellerNameDao.insert(seller)
            }
            RealEstateManagerModel.getDefaultRealEstates().forEach { estate ->
                realEstateDao.insert(estate)
            }
            RealEstateManagerModel.getDefaultImages().forEach { image ->
                imageDao.insert(image)
            }
        }
    }
}
