package com.openclassrooms.realestatemanager.database.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "images",

)
data class Media(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val uri: String,
    val description: String,
    val realEstateId: Long,
    val type: String,
)
