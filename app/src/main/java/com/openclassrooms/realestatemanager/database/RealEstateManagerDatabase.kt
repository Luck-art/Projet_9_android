package com.openclassrooms.realestatemanager.database

import androidx.room.Database
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
}

