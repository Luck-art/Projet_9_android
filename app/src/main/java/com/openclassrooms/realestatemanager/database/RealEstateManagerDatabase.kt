package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import com.openclassrooms.realestatemanager.database.tables.Media
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.database.tables.SellerName

@Database(entities = [RealEstate::class, Media::class, SellerName::class], version = 1)
abstract class RealEstateManagerDatabase : RoomDatabase() {
    abstract fun realEstateDao(): RealEstateDao
    abstract fun imageDao(): ImagesDao
    abstract fun sellerNameDao(): SellerNameDao

    companion object {
        @Volatile
        private var INSTANCE: RealEstateManagerDatabase? = null
    }
}

