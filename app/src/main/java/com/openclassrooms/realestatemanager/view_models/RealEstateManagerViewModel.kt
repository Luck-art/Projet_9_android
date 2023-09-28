import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.models.RealEstateManagerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class RealEstateManagerViewModel(
    private val realEstateDao: RealEstateDao,
    private val imageDao: ImagesDao,
    private val sellerNameDao: SellerNameDao
) : ViewModel() {
    val showDialog = MutableLiveData<Boolean>(false)

    init {
        viewModelScope.launch(Dispatchers.IO) {
            initializeDatabase()
        }
    }

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

    fun onAddButtonClicked() {
        showDialog.value = true
    }


    fun addNewRealEstate(estate: RealEstate) {
        viewModelScope.launch(Dispatchers.IO) {
            realEstateDao.insert(estate)
        }
    }


    fun onDialogDismissed() {
        showDialog.value = false
    }

    val viewState = realEstateDao.getAll()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())
}
