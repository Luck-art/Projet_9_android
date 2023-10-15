package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.database.tables.Media
import kotlinx.coroutines.flow.Flow

@Dao
interface ImagesDao {
    @Query("SELECT * FROM images WHERE realEstateId = :realEstateId")
    fun getImagesByRealEstateId(realEstateId: Long): List<Media>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
     fun insert(image: Media)

    @Query("SELECT * FROM images WHERE realEstateId = :realEstateId")
    fun observeImagesByRealEstateId(realEstateId: Long): Flow<List<Media>>

    @Query("SELECT * FROM images")
    fun getAllMedia(): Flow<List<Media>>

}
