package com.openclassrooms.realestatemanager.view_models

import RealEstateManagerViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val ViewModelsModule = module {
    viewModel { RealEstateManagerViewModel(get(), get(), get()) }
    viewModel { EstateDetailsViewModel(get(), get(), get()) }
}