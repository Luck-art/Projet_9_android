package com.openclassrooms.realestatemanager.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "seller_name")
data class SellerName(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
)
