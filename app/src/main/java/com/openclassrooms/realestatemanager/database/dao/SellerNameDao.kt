package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.openclassrooms.realestatemanager.database.tables.SellerName

    @Dao
    interface SellerNameDao {
        @Query("SELECT * FROM seller_name")
        fun getAll(): List<SellerName>

        @Insert(onConflict = OnConflictStrategy.IGNORE)
         fun insert(sellerName: SellerName): Long

        @Query("SELECT COUNT(*) FROM seller_name")
         fun getRowCount(): Int
    }