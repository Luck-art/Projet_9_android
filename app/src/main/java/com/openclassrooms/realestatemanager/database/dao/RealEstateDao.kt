package com.openclassrooms.realestatemanager.database.dao

import android.media.Image
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import kotlinx.coroutines.flow.Flow


@Dao
    interface RealEstateDao {
        @Query("SELECT * FROM real_estate")
        fun getAll(): Flow<List<RealEstate>>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
         fun insert(realEstate: RealEstate): Long

        @Query("SELECT COUNT(*) FROM real_estate")
         fun getRowCount(): Int
    }


