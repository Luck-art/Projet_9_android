package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.RawQuery
import androidx.room.Update
import androidx.sqlite.db.SupportSQLiteQuery
import com.openclassrooms.realestatemanager.database.tables.Media
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.database.tables.SellerName
import kotlinx.coroutines.flow.Flow


    @Dao
    interface RealEstateDao {
        @Query("SELECT * FROM real_estate")
        fun getAll(): Flow<List<RealEstate>>

        @Query("SELECT * FROM real_estate WHERE id = :id LIMIT 1")
        fun getOneItem(id : Long): RealEstate?

        @Query("SELECT * FROM real_estate WHERE id = :id LIMIT 1")
        fun getOneItemCursor(id : Long): Cursor?

        @Query("SELECT * FROM real_estate WHERE name = :name")
        fun getRealEstateByName(name: String): RealEstate

        @Insert(onConflict = OnConflictStrategy.IGNORE)
         fun insert(realEstate: RealEstate): Long

        @Query("SELECT COUNT(*) FROM real_estate")
        fun getRowCount(): Int

        @Query("SELECT * FROM real_estate WHERE id = :id LIMIT 1")
        fun observeOneItem(id : Long): Flow<RealEstate?>

        @Delete
        suspend fun delete(estate: RealEstate)

        @Query("SELECT MIN(price) FROM real_estate")
        fun getMinPrice(): Int?

        @Query("SELECT MAX(price) FROM real_estate")
        fun getMaxPrice(): Int?

        @Query("DELETE FROM real_estate WHERE id = :estateId")
        suspend fun deleteEstateById(estateId: Long)

        @Query("SELECT * FROM real_estate WHERE price BETWEEN :minPrice AND :maxPrice AND surface BETWEEN :minSurface AND :maxSurface AND rooms BETWEEN :minRooms AND :maxRooms")
        fun getFilteredRealEstates(minPrice: Double, maxPrice: Double, minSurface: Double, maxSurface: Double, minRooms: Double, maxRooms: Double): Flow<List<RealEstate>>


        @RawQuery(observedEntities = [RealEstate::class, Media::class, SellerName::class])
        fun filter(query: SupportSQLiteQuery): Flow<List<RealEstate>>


        @Update
        fun update(realEstate: RealEstate): Unit


    }


