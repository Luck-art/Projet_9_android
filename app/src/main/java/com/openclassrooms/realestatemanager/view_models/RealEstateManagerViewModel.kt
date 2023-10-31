import android.content.Context
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import com.openclassrooms.realestatemanager.database.tables.Media
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.models.DialogState
import com.openclassrooms.realestatemanager.models.RealEstateManagerModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlinx.coroutines.flow.map


class RealEstateManagerViewModel(
    private val context: Context,
    private val realEstateDao: RealEstateDao,
    private val imageDao: ImagesDao,
    private val sellerNameDao: SellerNameDao

) : ViewModel() {
    val showDialog = MutableLiveData<DialogState?>(null)
    val isInEditMode = MutableLiveData<Boolean>(false)
    val selectedRealEstate = MutableLiveData<RealEstate?>(null)


    init {
        viewModelScope.launch(Dispatchers.IO) {
            initializeDatabase(context)
        }
    }

    fun onItemClicked(item: RealEstate, isInEditMode: Boolean) {
        if (isInEditMode) {
            selectRealEstateForEditing(item)
        } else {

        }
    }

    fun updateRealEstate(estate: RealEstate) {
        viewModelScope.launch(Dispatchers.IO) {
            realEstateDao.update(estate)
        }
    }


    fun selectRealEstateForEditing(estate: RealEstate) {
        selectedRealEstate.value = estate
    }


    private suspend fun initializeDatabase(context: Context) {
        if (realEstateDao.getRowCount() == 0) {
            RealEstateManagerModel.getDefaultSellers().forEach { seller ->
                sellerNameDao.insert(seller)
            }
            RealEstateManagerModel.insertDefaultRealEstates(realEstateDao, imageDao, context)
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getAllRealEstateNames(): Flow<List<String>> {
        return viewState.map { list ->
            list.map { it.name }
        }
    }

    @RequiresApi(Build.VERSION_CODES.S)
    fun getRealEstateByName(name: String): Flow<RealEstate?> {
        return viewState.map { list ->
            list.find { it.name == name }
        }
    }





    fun onAddButtonClicked() {
        showDialog.value = DialogState.creation
    }

    fun onEditButtonClicked(id : Long) {
        viewModelScope.launch(Dispatchers.IO) {
            realEstateDao.getOneItem(id)?.let {
                withContext(Dispatchers.Main) {
                    showDialog.value = DialogState.edit(it)
                }
            }
        }
    }
    fun addNewRealEstate(estate: RealEstate) {
        viewModelScope.launch(Dispatchers.IO) {
            realEstateDao.insert(estate)
        }
        showDialog.value = null
    }

    fun toggleEditMode() {
        isInEditMode.value = !(isInEditMode.value ?: false)
    }

    fun editRealEstate(copy: RealEstate) {
        viewModelScope.launch(Dispatchers.IO) {
            realEstateDao.update(copy)
        }
        showDialog.value = null
    }





    val viewState = realEstateDao.getAll()
        .flowOn(Dispatchers.IO)
        .stateIn(viewModelScope, SharingStarted.Lazily, emptyList())

}
