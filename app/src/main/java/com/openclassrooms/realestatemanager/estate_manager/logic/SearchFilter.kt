package com.openclassrooms.realestatemanager.estate_manager.logic

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.EditText
import com.openclassrooms.realestatemanager.adapters.RealEstateManagerAdapter

class SearchFilter(val onTextChanged: (String) -> Unit) {

    private var isSearchBarVisible = false

    fun toggleSearchBar(searchEditText: EditText) {
        Log.d("toggleSearchBar", "Method Called")
        if (searchEditText.visibility == View.GONE) {
            searchEditText.visibility = View.VISIBLE
            searchEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                }

                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    onTextChanged(s.toString())
                }

                override fun afterTextChanged(s: Editable?) {
                }
            })
        } else {
            searchEditText.visibility = View.GONE
        }
    }

}

