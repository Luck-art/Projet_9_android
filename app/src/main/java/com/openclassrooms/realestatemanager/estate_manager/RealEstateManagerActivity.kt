package com.openclassrooms.realestatemanager.estate_manager

import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.app.AlertDialog
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
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateManagerModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class RealEstateManagerActivity : AppCompatActivity() {

    private lateinit var realEstateDao: RealEstateDao
    private lateinit var imageDao: ImagesDao
    private lateinit var sellerNameDao: SellerNameDao

    private val addButton: ImageView = findViewById(R.id.add_estate)



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

        //  RecyclerView
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
        addButton.setOnClickListener {
            showAddPropertyDialog()
        }
    }

    private fun showAddPropertyDialog() {
        val builder = AlertDialog.Builder(this)
        val inflater = layoutInflater
        val dialogLayout = inflater.inflate(R.layout.add_new_estate, null)

        val editImage = dialogLayout.findViewById<EditText>(R.id.editImageUrl)
        val editName = dialogLayout.findViewById<EditText>(R.id.editName)
        val editDescription = dialogLayout.findViewById<EditText>(R.id.editDescription)
        val editPrice = dialogLayout.findViewById<EditText>(R.id.editPrice)

        builder.setView(dialogLayout)
        builder.setPositiveButton("Ajouter") { dialogInterface, i ->
            val img = editImage.text.toString()
            val name = editName.text.toString()
            val description = editDescription.text.toString()
            val price = editPrice.text.toString().toInt()


            // Ajoutez le nouveau bien immobilier à la base de données
            CoroutineScope(Dispatchers.IO).launch {
                val newEstate = RealEstate(0, img, name, description, price)
                realEstateDao.insert(newEstate)
            }
        }
        builder.setNegativeButton("Annuler") { dialogInterface, i -> }
        builder.show()
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
