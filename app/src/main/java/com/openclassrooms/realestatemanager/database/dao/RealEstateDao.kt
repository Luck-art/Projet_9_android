package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import kotlinx.coroutines.flow.Flow


    @Dao
    interface RealEstateDao {
        @Query("SELECT * FROM real_estate")
        fun getAll(): Flow<List<RealEstate>>

        @Query("SELECT * FROM real_estate WHERE id = :id LIMIT 1")
        fun getOneItem(id : Long): RealEstate?

        @Query("SELECT * FROM real_estate WHERE name = :name")
        fun getRealEstateByName(name: String): RealEstate

        @Insert(onConflict = OnConflictStrategy.IGNORE)
         fun insert(realEstate: RealEstate): Long

        @Query("SELECT COUNT(*) FROM real_estate")
         fun getRowCount(): Int

        @Update
        fun update(realEstate: RealEstate): Unit


    }


