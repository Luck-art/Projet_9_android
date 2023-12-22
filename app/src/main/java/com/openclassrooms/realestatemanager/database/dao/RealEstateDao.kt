package com.openclassrooms.realestatemanager.database.dao

import android.database.Cursor
import androidx.room.Dao
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


    @Query("SELECT MIN(price) FROM real_estate")
    fun getMinPrice(): Int?

    @Query("SELECT MAX(price) FROM real_estate")
    fun getMaxPrice(): Int?

    @Query("SELECT MIN(surface) FROM real_estate")
    fun getMinSurface(): Int?

    @Query("SELECT MAX(surface) FROM real_estate")
    fun getMaxSurface(): Int?

    @Query("SELECT MIN(rooms) FROM real_estate")
    fun getMinRooms(): Int?

    @Query("SELECT MAX(rooms) FROM real_estate")
    fun getMaxRooms(): Int?

    @Query("SELECT * FROM real_estate WHERE latitude BETWEEN :latMin AND :latMax AND longitude BETWEEN :lngMin AND :lngMax")
    fun getRealEstatesInRadius(latMin: Double, latMax: Double, lngMin: Double, lngMax: Double): List<RealEstate>



    @Query("SELECT * FROM real_estate WHERE price BETWEEN :minPrice AND :maxPrice AND surface BETWEEN :minSurface AND :maxSurface AND rooms BETWEEN :minRooms AND :maxRooms AND (:isSold IS NULL OR sended = :isSold) AND (:estateType IS NULL OR estate_type = :estateType)" +
            "AND (point_interest LIKE '%' || :School || '%' OR :School IS NULL) AND (point_interest LIKE '%' || :Park || '%' OR :Park IS NULL) AND (point_interest LIKE '%' || :Restaurant || '%' OR :Restaurant IS NULL) AND (point_interest LIKE '%' || :Sport || '%' OR :Sport IS NULL)" +
            "AND (point_interest LIKE '%' || :Shop || '%' OR :Shop IS NULL) AND (point_interest LIKE '%' || :FastFood || '%' OR :FastFood IS NULL)")
    fun getFilteredRealEstates(
        minPrice: Double,
        maxPrice: Double,
        minSurface: Double,
        maxSurface: Double,
        minRooms: Double,
        maxRooms: Double,
        isSold: Boolean?,
        estateType: String?,
        School: String?,
        Park: String?,
        Restaurant: String?,
        Sport: String?,
        Shop: String?,
        FastFood: String?,
    ): Flow<List<RealEstate>>

    @RawQuery(observedEntities = [RealEstate::class, Media::class, SellerName::class])
    fun filter(query: SupportSQLiteQuery): Flow<List<RealEstate>>


    @Update
    fun update(realEstate: RealEstate): Unit


}