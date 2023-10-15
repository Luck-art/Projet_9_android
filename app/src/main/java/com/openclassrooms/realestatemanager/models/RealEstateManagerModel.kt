package com.openclassrooms.realestatemanager.models

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

    fun getDefaultRealEstates(): List<RealEstate> {
        return listOf(
            RealEstate(img ="https://managecasa.com/wp-content/uploads/2021/02/shutterstock_1731900589-e1612637876218-995x460.jpg", name = "Tall house", description = "17 beautiful av, California", price = 15000),
            RealEstate(img ="https://images.pexels.com/photos/323780/pexels-photo-323780.jpeg?auto=compress&cs=tinysrgb&w=600", name = "Modern house", description = "25 beautiful av, California", price = 15000),
            RealEstate(img = "https://www.trulia.com/pictures/thumbs_5/zillowstatic/fp/3cbe9c6ede199cab234379ca9901a7a9-full.jpg", name = "Sweet home", description = "5 George Washington blv, New York",  price = 150000),
        )
    }

    fun getDefaultImages(): List<Media> {
        return listOf(
            Media(0, "https://managecasa.com/wp-content/uploads/2021/02/shutterstock_1731900589-e1612637876218-995x460.jpg", "Tall house California", 0, type = "image"),
            Media(1, "https://images.pexels.com/photos/323780/pexels-photo-323780.jpeg?auto=compress&cs=tinysrgb&w=600", "Modern house, California", 1, type = "image"),
            Media(1, "https://www.trulia.com/pictures/thumbs_5/zillowstatic/fp/3cbe9c6ede199cab234379ca9901a7a9-full.jpg", "Sweet home, New York", 2, type = "image"),
            Media(2, "https://player.vimeo.com/external/539011265.sd.mp4?s=ac4e448f51f5a518ea1b971f79fc7a9986f2a359&profile_id=164&oauth2_token_id=57447761", "Sweet home, New York", 1, type = "video"),

            )
    }
}