package com.openclassrooms.realestatemanager.estate_manager

import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import com.openclassrooms.realestatemanager.database.tables.Images
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.database.tables.SellerName

class RealEstateManagerActivity {
    suspend fun initializeDatabase(realEstateDao: RealEstateDao, imageDao: ImagesDao, sellerNameDao: SellerNameDao) {
        if (realEstateDao.getRowCount() == 0) {
            // Générer quelques noms de vendeurs et les insérer
            val sellerId1 = sellerNameDao.insert(SellerName("Vendeur 1"))
            val sellerId2 = sellerNameDao.insert(SellerName("Vendeur 2"))

            // Générer quelques biens immobiliers et les insérer
            val realEstateId1 = realEstateDao.insert(RealEstate())
            val realEstateId2 = realEstateDao.insert(RealEstate())

            // Générer quelques images pour chaque bien immobilier
            imageDao.insert(Image("url1", realEstateId1))
            imageDao.insert(Image("url2", realEstateId1))
            imageDao.insert(Image("url3", realEstateId2))
        }
    }
}
