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
            RealEstate(0, "Tall house", "17 beautiful av, California", 100000),
            RealEstate(1, "Sweet home", "5 George Washington blv, New York", 150000)
        )
    }

    fun getDefaultImages(): List<Images> {
        return listOf(
            Images(0, "https://managecasa.com/wp-content/uploads/2021/02/shutterstock_1731900589-e1612637876218-995x460.jpg", "Tall house California", 0),
            Images(1, "https://www.trulia.com/pictures/thumbs_5/zillowstatic/fp/3cbe9c6ede199cab234379ca9901a7a9-full.jpg", "Sweet home, New York", 1)
        )
    }
}