package com.openclassrooms.realestatemanager.models

import com.openclassrooms.realestatemanager.database.tables.RealEstate

sealed interface DialogState {
    object creation : DialogState
    data class edit(val estate : RealEstate) : DialogState
}