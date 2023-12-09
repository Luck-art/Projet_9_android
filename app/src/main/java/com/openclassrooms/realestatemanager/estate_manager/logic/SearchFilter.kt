package com.openclassrooms.realestatemanager.estate_manager.logic

import android.content.Context
import android.widget.LinearLayout
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

    fun showFilterDialog() {
        // Créer un AlertDialog.Builder et définir son titre
        val builder = AlertDialog.Builder(context)
        builder.setTitle("Filtrer les propriétés")

        // Créer un LinearLayout pour contenir les RangeSliders
        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val padding = context.resources.getDimensionPixelSize(R.dimen.default_padding)
            setPadding(padding, padding, padding, padding)
        }

        // Créer le RangeSlider pour le prix
        val priceRangeSlider = RangeSlider(context).apply {
            // Configurer le priceRangeSlider ici
        }
        linearLayout.addView(priceRangeSlider)

        // Créer le RangeSlider pour la surface
        val surfaceRangeSlider = RangeSlider(context).apply {
            // Configurer le surfaceRangeSlider ici
        }
        linearLayout.addView(surfaceRangeSlider)

        // Ajouter le LinearLayout au builder
        builder.setView(linearLayout)

        // Définir les boutons de la boîte de dialogue
        builder.setPositiveButton("OK") { _, _ ->
            val selectedMinPrice = priceRangeSlider.values[0]
            val selectedMaxPrice = priceRangeSlider.values[1]
            val selectedMinSurface = surfaceRangeSlider.values[0]
            val selectedMaxSurface = surfaceRangeSlider.values[1]
            onFilterSelected(selectedMinPrice, selectedMaxPrice, selectedMinSurface, selectedMaxSurface)
        }
        builder.setNegativeButton("Annuler", null)

        // Afficher la boîte de dialogue
        builder.create().show()
    }

    private fun onFilterSelected(minPrice: Float, maxPrice: Float, minSurface: Float, maxSurface: Float) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredEstates = realEstateDao.getFilteredRealEstates(minPrice.toDouble(), maxPrice.toDouble(), minSurface.toDouble(), maxSurface.toDouble()).first()
            withContext(Dispatchers.Main) {
                onUpdateUI(filteredEstates)
            }
        }
    }
}





