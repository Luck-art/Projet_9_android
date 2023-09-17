package com.openclassrooms.realestatemanager.estate_manager

import com.openclassrooms.realestatemanager.database.dao.ImageDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import com.openclassrooms.realestatemanager.models.RealEstateManagerModel


class RealEstateManagerActivity {
    suspend fun initializeDatabase(realEstateDao: RealEstateDao, imageDao: ImageDao, sellerNameDao: SellerNameDao) {
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

}
