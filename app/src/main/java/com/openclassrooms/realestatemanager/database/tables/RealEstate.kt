package com.openclassrooms.realestatemanager.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "real_estate")
data class RealEstate(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val img: String,
    val name: String,
    val description : String,
    val price: Int
)
