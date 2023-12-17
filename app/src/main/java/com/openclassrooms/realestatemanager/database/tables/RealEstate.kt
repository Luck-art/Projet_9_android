package com.openclassrooms.realestatemanager.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.openclassrooms.realestatemanager.database.other.Converters
import java.util.Date

@Entity(tableName = "real_estate")
@TypeConverters(Converters::class)
data class RealEstate(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val img: String,
    val estate_type: String,
    val name: String,
    val description: String,
    val address: String,
    val price: Int,
    val latitude: Double? = null,
    val longitude: Double? = null,
    val surface: Double? = null,
    val rooms: Int,
    val estate_agent: String,
    var point_interest: List<String>? = null,
    var date_sale: Date? = null,
    var date_sold: Date? = null,
    val sended: Boolean = true,
)
