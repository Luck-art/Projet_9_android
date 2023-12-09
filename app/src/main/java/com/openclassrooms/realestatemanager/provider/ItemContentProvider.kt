package com.openclassrooms.realestatemanager.provider

import android.content.ContentProvider
import android.content.ContentUris
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.net.Uri
import androidx.room.Room
import com.openclassrooms.realestatemanager.database.RealEstateManagerDatabase
import org.jetbrains.annotations.VisibleForTesting

class ItemContentProvider : ContentProvider() {
    companion object {
        val AUTHORITY = "com.openclassrooms.realestatemanager.provider"

        val TABLE_NAME: String = "real_estate"

        val URI_ITEM = Uri.parse("content://$AUTHORITY/$TABLE_NAME")

        @VisibleForTesting
        internal var database: RealEstateManagerDatabase? = null
    }

    override fun onCreate(): Boolean {
        return true
    }

    private fun createDatabase(context: Context): RealEstateManagerDatabase {
        if (database == null) {
            val newDatabase = Room.databaseBuilder(
                context,
                RealEstateManagerDatabase::class.java, "database-name"
            ).build()
            database = newDatabase
            return newDatabase
        } else {
            return database!!
        }
    }

    override fun query(
        uri: Uri,
        projection: Array<String?>?,
        selection: String?,
        selectionArgs: Array<String?>?,
        sortOrder: String?
    ): Cursor? {
        context?.let { context ->
            val estateId = ContentUris.parseId(uri)

            val database = createDatabase(context)

            val cursor = database.realEstateDao().getOneItemCursor(estateId)
            cursor?.setNotificationUri(context.contentResolver, uri)
            return cursor
        }
        throw IllegalArgumentException("Failed to query row for uri $uri")
    }

    override fun getType(uri: Uri): String {
        return "vnd.android.cursor.item/$AUTHORITY.$TABLE_NAME"
    }

    override fun insert(uri: Uri, contentValues: ContentValues?): Uri {
        throw IllegalArgumentException("not implemented")
    }

    override fun delete(uri: Uri, s: String?, strings: Array<String?>?): Int {
        throw IllegalArgumentException("not implemented")
    }

    override fun update(
        uri: Uri,
        contentValues: ContentValues?,
        s: String?,
        strings: Array<String?>?
    ): Int {
        throw IllegalArgumentException("not implemented")
    }
}