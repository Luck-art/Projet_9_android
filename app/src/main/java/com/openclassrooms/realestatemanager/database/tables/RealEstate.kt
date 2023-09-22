package com.openclassrooms.realestatemanager.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "real_estate")
data class RealEstate(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val img: String,
    val name: String,
    val description : String,
    val location: String,
    val price: Int
)
