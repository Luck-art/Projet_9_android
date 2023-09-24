package com.openclassrooms.realestatemanager.view_models

import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.dsl.module

val ViewModelsModule = module {
    viewModelOf(::RealEstateManagerViewModel)
}