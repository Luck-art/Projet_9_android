package com.openclassrooms.realestatemanager.models

import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.tables.Media
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.database.tables.SellerName

object RealEstateManagerModel {
    fun getDefaultSellers(): List<SellerName> {
        return listOf(
            SellerName(0, "William"),
            SellerName(1, "Damien")
        )
    }

    fun insertDefaultRealEstates(dao: RealEstateDao, imageDao: ImagesDao) {
        val id1 = dao.insert(
            RealEstate(
                img = "https://managecasa.com/wp-content/uploads/2021/02/shutterstock_1731900589-e1612637876218-995x460.jpg",
                name = "Tall house",
                description = "17 beautiful av, California",
                price = 15000
            )
        )
        imageDao.insert(
            Media(
                0,
                "https://managecasa.com/wp-content/uploads/2021/02/shutterstock_1731900589-e1612637876218-995x460.jpg",
                "Tall house California",
                realEstateId = id1,
                type = "image"
            )
        )
        imageDao.insert(
            Media(
                0,
                "https://managecasa.com/wp-content/uploads/2021/02/shutterstock_1731900589-e1612637876218-995x460.jpg",
                "Tall house California",
                realEstateId = id1,
                type = "image"
            )
        )
        imageDao.insert(
            Media(
                0,
                "https://player.vimeo.com/external/539011265.sd.mp4?s=ac4e448f51f5a518ea1b971f79fc7a9986f2a359&profile_id=164&oauth2_token_id=57447761",
                "Sweet home, New York",
                realEstateId = id1,
                type = "video"
            )
        )


        val id2 = dao.insert(
            RealEstate(
                img = "https://images.pexels.com/photos/323780/pexels-photo-323780.jpeg?auto=compress&cs=tinysrgb&w=600",
                name = "Modern house",
                description = "25 beautiful av, California",
                price = 15000
            )
        )
        imageDao.insert(
            Media(
                0,
                "https://images.pexels.com/photos/323776/pexels-photo-323776.jpeg?auto=compress&cs=tinysrgb&w=600",
                "Modern house, California",
                realEstateId = id2,
                type = "image"
            ),
        )
        imageDao.insert(
            Media(
                0,
                "https://images.pexels.com/photos/6489116/pexels-photo-6489116.jpeg?auto=compress&cs=tinysrgb&w=600",
                "Modern house, California",
                realEstateId = id2,
                type = "image"
            ),
        )
        imageDao.insert(
            Media(
                0,
                "https://player.vimeo.com/external/539011132.sd.mp4?s=e2c99fdd496f43a2db177a714dfdefc85ae210ed&profile_id=164&oauth2_token_id=57447761",
                "Sweet home, New York",
                realEstateId = id2,
                type = "video"
            )
        )

        val id3 = dao.insert(
            RealEstate(
                img = "https://images.pexels.com/photos/323780/pexels-photo-323780.jpeg?auto=compress&cs=tinysrgb&w=600",
                name = "Modern house",
                description = "25 beautiful av, California",
                price = 15000
            )
        )
        imageDao.insert(
            Media(
                2,
                "https://www.trulia.com/pictures/thumbs_5/zillowstatic/fp/3cbe9c6ede199cab234379ca9901a7a9-full.jpg",
                "Sweet home, New York",
                realEstateId = id3,
                type = "image"
            )
        )
    }
}