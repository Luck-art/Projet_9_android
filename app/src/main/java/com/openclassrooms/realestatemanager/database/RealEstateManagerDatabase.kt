package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import com.openclassrooms.realestatemanager.database.other.Converters
import com.openclassrooms.realestatemanager.database.tables.Media
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.database.tables.SellerName


@Database(entities = [RealEstate::class, Media::class, SellerName::class], version = 1)
@TypeConverters(Converters::class)
abstract class RealEstateManagerDatabase : RoomDatabase() {
    abstract fun realEstateDao(): RealEstateDao
    abstract fun imageDao(): ImagesDao
    abstract fun sellerNameDao(): SellerNameDao

    companion object {
        @Volatile
        private var INSTANCE: RealEstateManagerDatabase? = null

        fun getInstance(context: Context): RealEstateManagerDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RealEstateManagerDatabase::class.java,
                    "real_estate_manager_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }


    }


}

