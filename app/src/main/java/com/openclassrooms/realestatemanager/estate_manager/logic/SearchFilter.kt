package com.openclassrooms.realestatemanager.estate_manager.logic

import android.view.View
import android.widget.EditText
import com.openclassrooms.realestatemanager.adapters.RealEstateManagerAdapter

class SearchFilter(private val adapter: RealEstateManagerAdapter) {

    fun showSearchEditText(searchEditText: EditText) {
        searchEditText.visibility = View.VISIBLE
    }

    fun hideSearchEditText(searchEditText: EditText) {
        searchEditText.visibility = View.GONE
    }

    fun filterItems(text: String) {
        adapter.filterItems(text)
    }

}
