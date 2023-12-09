package com.openclassrooms.realestatemanager.database.tables

import android.location.Address
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "real_estate")
data class RealEstate(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val img: String,
    val name: String,
    val description : String,
    val address: String,
    val price: Int,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val surface: Double? = null,
    val rooms: Int,
    val sended : Boolean = true,
)
