package com.openclassrooms.realestatemanager.database.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.openclassrooms.realestatemanager.database.tables.SellerName

interface SellerNameDao {
    @Dao
    interface SellerNameDao {
        @Query("SELECT * FROM seller_name")
        fun getAll(): List<SellerName>

        @Insert
        fun insert(sellerName: SellerName): Long
    }
}