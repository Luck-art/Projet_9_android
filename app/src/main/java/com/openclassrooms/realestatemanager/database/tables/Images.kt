package com.openclassrooms.realestatemanager.database.tables

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.PrimaryKey

@Entity(
    tableName = "images",

)
data class Images(
    @PrimaryKey(autoGenerate = true)
    val id: Long,
    val uri: String,
    val description: String,
    val realEstateId: Long
)
