package com.openclassrooms.realestatemanager.estate_manager.logic

import android.content.Context
import androidx.appcompat.app.AlertDialog
import com.google.android.material.slider.RangeSlider
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class SearchFilter(
    val context: Context,
    private val realEstateDao: RealEstateDao,
    private val onUpdateUI: (List<RealEstate>) -> Unit
) {

    fun showPriceFilterDialog() {
        val rangeSlider = RangeSlider(context)

        val dialog = AlertDialog.Builder(context)
            .setTitle("Filtrer par Prix")
            .setView(rangeSlider)
            .setPositiveButton("OK") { _, _ ->
                if (rangeSlider.values.size >= 2) {
                    val selectedMinPrice = rangeSlider.values[0]
                    val selectedMaxPrice = rangeSlider.values[1]
                    onPriceRangeSelected(selectedMinPrice, selectedMaxPrice)
                }
            }
            .setNegativeButton("Annuler", null)
            .create()

        dialog.show()
    }

    private fun onPriceRangeSelected(minPrice: Float, maxPrice: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredEstates = realEstateDao.getRealEstatesByPriceRange(minPrice.toDouble(), maxPrice.toDouble()).first()
            withContext(Dispatchers.Main) {
                onUpdateUI(filteredEstates)
            }
        }
    }
}




