package com.openclassrooms.realestatemanager.estate_manager.logic

import android.content.Context
import android.widget.LinearLayout
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import com.google.android.material.slider.RangeSlider
import com.openclassrooms.realestatemanager.R
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

    private var minPrice: Float = 0f
    private var maxPrice: Float = 1000000f

    fun showFilterDialog() {
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Filtrer les propriétés")

        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val padding = context.resources.getDimensionPixelSize(R.dimen.default_padding)
            setPadding(padding, padding, padding, padding)
        }

        val priceLabel = TextView(context).apply {
            text = context.getString(R.string.price_filter_label)
        }
        linearLayout.addView(priceLabel)

        val priceRangeSlider = RangeSlider(context).apply {
            valueFrom = minPrice
            valueTo = maxPrice
            setValues(minPrice, maxPrice)
        }

        linearLayout.addView(priceRangeSlider)

        val surfaceLabel = TextView(context).apply {
            text = context.getString(R.string.surface_filter_label)
        }
        linearLayout.addView(surfaceLabel)

        val surfaceRangeSlider = RangeSlider(context).apply {
        }
        linearLayout.addView(surfaceRangeSlider)

        builder.setView(linearLayout)

        builder.setPositiveButton("OK") { _, _ ->
            val selectedMinPrice = priceRangeSlider.values[0]
            val selectedMaxPrice = priceRangeSlider.values[1]
            val selectedMinSurface = surfaceRangeSlider.values[0]
            val selectedMaxSurface = surfaceRangeSlider.values[1]
            val selectedMinRooms = surfaceRangeSlider.values[0]
            val selectedMaxRooms = surfaceRangeSlider.values[1]
            onFilterSelected(selectedMinPrice, selectedMaxPrice, selectedMinSurface, selectedMaxSurface, selectedMinRooms, selectedMaxRooms)
        }
        builder.setNegativeButton("Annuler", null)

        builder.create().show()
    }

    private fun onFilterSelected(minPrice: Float, maxPrice: Float, minSurface: Float, maxSurface: Float, minRooms: Float, maxRooms: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredEstates = realEstateDao.getFilteredRealEstates(minPrice.toDouble(), maxPrice.toDouble(), minSurface.toDouble(), maxSurface.toDouble(), minRooms.toDouble(), maxRooms.toDouble()).first()
            withContext(Dispatchers.Main) {
                onUpdateUI(filteredEstates)
            }
        }
    }

    fun fetchPriceRangeAndShowDialog() {
        CoroutineScope(Dispatchers.IO).launch {
            val minPriceValue = realEstateDao.getMinPrice()?.toFloat() ?: 0f
            val maxPriceValue = realEstateDao.getMaxPrice()?.toFloat() ?: 1000000f

            withContext(Dispatchers.Main) {
                minPrice = minPriceValue
                maxPrice = maxPriceValue
                showFilterDialog()
            }
        }
    }

    fun updatePriceRange(minPrice: Int, maxPrice: Int) {
        this.minPrice = minPrice.toFloat()
        this.maxPrice = maxPrice.toFloat()
    }

}





