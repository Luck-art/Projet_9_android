package com.openclassrooms.realestatemanager.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.openclassrooms.realestatemanager.database.dao.ImagesDao
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.dao.SellerNameDao
import com.openclassrooms.realestatemanager.database.tables.Media
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.database.tables.SellerName

@Database(entities = [RealEstate::class, Media::class, SellerName::class], version = 1)
abstract class RealEstateManagerDatabase : RoomDatabase() {
    abstract fun realEstateDao(): RealEstateDao
    abstract fun imageDao(): ImagesDao
    abstract fun sellerNameDao(): SellerNameDao

    companion object {
        // Volatile pour assurer la visibilité des modifications dans tous les threads
        @Volatile
        private var INSTANCE: RealEstateManagerDatabase? = null

        // Fonction pour obtenir l'instance de la base de données
        fun getDatabase(context: Context): RealEstateManagerDatabase {
            // Si l'instance n'est pas nulle, la retourner
            // Sinon, créer la base de données
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    RealEstateManagerDatabase::class.java,
                    "realestate_manager_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}

