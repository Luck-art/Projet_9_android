import android.util.Log
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DeleteEstate(public val estateDao: RealEstateDao, private val scope: CoroutineScope) {

    fun deleteEstate(estateId: Long) {
        scope.launch(Dispatchers.IO) {
            estateDao.deleteEstateById(estateId)
        }
    }
}

