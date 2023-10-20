package com.openclassrooms.realestatemanager.database

import androidx.room.Room
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module

val DatabaseModule = module {
    single<RealEstateManagerDatabase> {
        Room.databaseBuilder(
            androidContext(),

            RealEstateManagerDatabase::class.java, "database-name"
        ).build()
    }
    single<ImagesDao> {
        val db : RealEstateManagerDatabase = get()
        db.imageDao()
    }
    single<RealEstateDao> {
        val db : RealEstateManagerDatabase = get()
        db.realEstateDao()
    }
    single<SellerNameDao>  {
        val db : RealEstateManagerDatabase = get()
        db.sellerNameDao()
    }
}