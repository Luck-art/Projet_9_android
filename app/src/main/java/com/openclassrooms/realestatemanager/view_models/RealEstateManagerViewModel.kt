package com.openclassrooms.realestatemanager.view_models

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import com.openclassrooms.realestatemanager.models.RealEstateManagerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch


class RealEstateManagerViewModel(
    private  val realEstateDao: RealEstateDao,
    private  val imageDao: ImagesDao,
    private  val sellerNameDao: SellerNameDao
) : ViewModel() {
    private suspend fun initializeDatabase() {
        if (realEstateDao.getRowCount() == 0) {
            RealEstateManagerModel.getDefaultSellers().forEach { seller ->
                sellerNameDao.insert(seller)
            }
            RealEstateManagerModel.getDefaultRealEstates().forEach { estate ->
                realEstateDao.insert(estate)
            }
            RealEstateManagerModel.getDefaultImages().forEach { image ->
                imageDao.insert(image)
            }
        }
    }

    init {
        viewModelScope.launch(Dispatchers.IO) {
            initializeDatabase()
        }
    }

    val viewState = realEstateDao.getAll().flowOn(Dispatchers.IO).stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

}