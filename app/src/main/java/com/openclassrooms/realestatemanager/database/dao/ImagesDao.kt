package com.openclassrooms.realestatemanager.database.dao

import android.media.Image
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

interface ImagesDao {
    @Dao
    interface ImageDao {
        @Query("SELECT * FROM images WHERE realEstateId = :realEstateId")
        fun getImagesByRealEstateId(realEstateId: Long): List<Image>

        @Insert
        fun insert(image: Image)
    }
}