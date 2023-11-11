package com.openclassrooms.realestatemanager.view_models

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.tables.Media
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateManagerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class EstateDetailsViewModel(private val estateDao : RealEstateDao, private val savedStateHandle: SavedStateHandle, private val imagesDao: ImagesDao) : ViewModel() {

    data class EstateDetailsViewState(val realEstate: RealEstate?, val medias: List<Media>)


    val state: StateFlow<EstateDetailsViewState?> =  estateDao.observeOneItem(id = savedStateHandle.get<Long>("estate_id")!!).combine( imagesDao.observeImagesByRealEstateId(realEstateId = savedStateHandle.get<Long>("estate_id")!!)) { estate, medias ->
        EstateDetailsViewState(estate, medias)
    }
        .flowOn(
        Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, null)

    fun insertMedia(media: Media) {
        viewModelScope.launch(Dispatchers.IO) {
            imagesDao.insert(media)
            
        }
    }

}