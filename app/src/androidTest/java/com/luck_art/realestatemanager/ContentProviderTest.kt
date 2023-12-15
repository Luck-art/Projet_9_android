package com.luck_art.realestatemanager

import android.content.ContentResolver
import android.content.ContentUris
import android.content.ContentValues
import android.net.Uri
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.provider.ItemContentProvider
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ContentProviderTest {
    // FOR DATA
    private var mContentResolver: ContentResolver? = null

    @Before
    fun setUp() {
        ItemContentProvider.database = inMemoryDatabaseBuilder(
            InstrumentationRegistry.getInstrumentation().context,
            RealEstateManagerDatabase::class.java
        ).allowMainThreadQueries().build()

        mContentResolver = InstrumentationRegistry.getInstrumentation().context.contentResolver
    }

    @Test
    fun insertAndGetItem() {
        // BEFORE : Adding demo item
        ItemContentProvider.database?.realEstateDao()?.insert(
            RealEstate(
                id = 1234,
                img = "the_img",
                name = "the_name",
                description = "the_description",
                address = "the_address",
                price = 100,
                latitude = null,
                longitude = null,
                sended = false,
                rooms = 0,
                surface = 100.0,
            )
        )

        // TEST
        val cursor = mContentResolver!!.query(
            ContentUris.withAppendedId(
                ItemContentProvider.URI_ITEM,
                1234,
            ), null, null, null, null
        )

        assert(cursor != null)
        assert(cursor!!.count == 1)
        cursor.moveToFirst()

        val name = cursor.getString(cursor.getColumnIndexOrThrow("name"))
        assert(name == "the_name")
    }

    @Test
    fun insertAndGetItem_differentId_shouldNotWork() {
        // BEFORE : Adding demo item
        ItemContentProvider.database?.realEstateDao()?.insert(
            RealEstate(
                id = 6666,
                img = "the_img",
                name = "the_name",
                description = "the_description",
                address = "the_address",
                price = 100,
                latitude = null,
                longitude = null,
                sended = false,
                rooms = 0,
                surface = 100.0,
            )
        )

        // TEST
        val cursor = mContentResolver!!.query(
            ContentUris.withAppendedId(
                ItemContentProvider.URI_ITEM,
                1234,
            ), null, null, null, null
        )

        assert(cursor != null)
        assert(cursor!!.count == 0)
    }
}