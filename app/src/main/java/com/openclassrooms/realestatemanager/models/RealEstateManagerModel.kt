package com.openclassrooms.realestatemanager.models

import android.content.Context
import android.location.Geocoder
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.tables.Media
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.database.tables.SellerName
import java.util.Locale

object RealEstateManagerModel {
    fun getDefaultSellers(): List<SellerName> {
        return listOf(
            SellerName(0, "William"),
            SellerName(1, "Damien")
        )
    }

    fun getCoordinatesFromAddress(context: Context, addressString: String): Pair<Double, Double> {
        val geocoder = Geocoder(context, Locale.getDefault())
        try {
            val addressList: List<android.location.Address>? = geocoder.getFromLocationName(addressString, 1)
            if (!addressList.isNullOrEmpty()) {
                val address = addressList[0]
                return Pair(address.latitude, address.longitude)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return Pair(0.0, 0.0)
    }



    fun insertDefaultRealEstates(dao: RealEstateDao, imageDao: ImagesDao, context: Context) {


        val (latitude, longitude) = getCoordinatesFromAddress(context, "2620 Main St, Santa Monica, CA 90405")
        val (latitude2, longitude2) = getCoordinatesFromAddress(context, "6801 Hollywood Blvd, Los Angeles, CA 90028")
        val (latitude3, longitude3) = getCoordinatesFromAddress(context, "30 Rockefeller Plaza, New York, NY 10112")


        val id1 = dao.insert(
            RealEstate(
                id = 0,
                img = "https://managecasa.com/wp-content/uploads/2021/02/shutterstock_1731900589-e1612637876218-995x460.jpg",
                name = "Tall house",
                description = "the very tall house",
                address = "2620 Main St, Santa Monica, CA 90405",
                price = 15000,
                latitude = latitude,
                longitude = longitude,
                surface = 2.5,
                rooms = 6,
                sended = true,
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
                id = 1,
                img = "https://images.pexels.com/photos/323780/pexels-photo-323780.jpeg?auto=compress&cs=tinysrgb&w=600",
                name = "Modern house",
                description = "modern style",
                address = "6801 Hollywood Blvd, Los Angeles, CA 90028",
                price = 30000,
                latitude = latitude2,
                longitude = longitude2,
                surface = 2.0,
                rooms = 5,
                sended = false,
            )
        )
        imageDao.insert(
            Media(
                0,
                "https://images.pexels.com/photos/323776/pexels-photo-323776.jpeg?auto=compress&cs=tinysrgb&w=600",
                "modern house",
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
                id = 2,
                img = "https://images.pexels.com/photos/323780/pexels-photo-323780.jpeg?auto=compress&cs=tinysrgb&w=600",
                name = "Sweet home",
                description = "sweety !",
                address = "30 Rockefeller Plaza, New York, NY 10112",
                price = 15000,
                latitude = latitude3,
                longitude = longitude3,
                surface = 2.5,
                rooms = 6,
                sended = false
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