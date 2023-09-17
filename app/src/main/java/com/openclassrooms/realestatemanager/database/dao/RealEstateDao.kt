package com.openclassrooms.realestatemanager.database.dao

import android.media.Image
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.database.tables.RealEstate

interface RealEstateDao {
    @Dao
    interface RealEstateDao {
        @Query("SELECT * FROM real_estate")
        fun getAll(): List<RealEstate>

        @Insert
        fun insert(realEstate: RealEstate): Long
    }

    @Dao
    interface ImageDao {
        @Query("SELECT * FROM images WHERE realEstateId = :realEstateId")
        fun getImagesByRealEstateId(realEstateId: Long): List<Image>

        @Insert
        fun insert(image: Image)
    }

}