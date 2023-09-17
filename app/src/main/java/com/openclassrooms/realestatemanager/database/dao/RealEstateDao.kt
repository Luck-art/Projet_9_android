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

        @Query("SELECT COUNT(*) FROM real_estate")
        fun getRowCount(): Int

        @Insert
        fun insert(realEstate: RealEstate): Long
    }

}