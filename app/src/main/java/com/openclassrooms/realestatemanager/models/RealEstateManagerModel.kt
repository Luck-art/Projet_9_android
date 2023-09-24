package com.openclassrooms.realestatemanager.models

import com.openclassrooms.realestatemanager.database.tables.Images
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
            RealEstate(id = 0, img ="Tall house", name = "17 beautiful av, California", description = "Tall house description", price = 15000),
            RealEstate(id = 1, img = "Sweet home", name = "5 George Washington blv, New York", description = "Sweet home description",  price = 150000)
        )
    }

    fun getDefaultImages(): List<Images> {
        return listOf(
            Images(0, "https://managecasa.com/wp-content/uploads/2021/02/shutterstock_1731900589-e1612637876218-995x460.jpg", "Tall house California", 0),
            Images(1, "https://www.trulia.com/pictures/thumbs_5/zillowstatic/fp/3cbe9c6ede199cab234379ca9901a7a9-full.jpg", "Sweet home, New York", 1)
        )
    }
}