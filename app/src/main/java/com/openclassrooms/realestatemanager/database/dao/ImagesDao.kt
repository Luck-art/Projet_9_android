package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.database.tables.Images

@Dao
interface ImagesDao {
    @Query("SELECT * FROM images WHERE realEstateId = :realEstateId")
    fun getImagesByRealEstateId(realEstateId: Long): List<Images>

    @Insert
    fun insert(image: Images)
}
