package com.openclassrooms.realestatemanager.estate_manager.logic

import android.content.Context
import android.graphics.Color
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
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
import kotlin.math.roundToInt

class SearchFilter(
    val context: Context,
    private val realEstateDao: RealEstateDao,
    private val onUpdateUI: (List<RealEstate>) -> Unit
) {

    companion object {
        val PRICE_ID = View.generateViewId()
        val SURFACE_ID = View.generateViewId()
    }

    fun showFilterDialog(
        minPrice: Float,
        maxPrice: Float,
        minSurface: Float,
        maxSurface: Float,
        minRooms: Float,
        maxRooms: Float
    ) {
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
            id = PRICE_ID
            valueFrom = minPrice
            valueTo = maxPrice
            setValues(minPrice, maxPrice)
        }
        linearLayout.addView(priceRangeSlider)

        val priceFormatter: NumberFormat = NumberFormat.getCurrencyInstance().apply {
            maximumFractionDigits = 0
            currency = Currency.getInstance("EUR")
        }

        linearLayout.addView(FrameLayout(context).apply {
            val minText = TextView(context).apply {
                setTextColor(Color.BLACK)
            }
            val maxText = TextView(context).apply {
                setTextColor(Color.BLACK)
            }
            // the listener
            priceRangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { rangeSlider, _, _ ->
                minText.text = priceFormatter.format(rangeSlider.values[0])
                maxText.text = priceFormatter.format(rangeSlider.values[1])
            })
            // the initial value
            minText.text = priceFormatter.format(priceRangeSlider.values[0])
            maxText.text = priceFormatter.format(priceRangeSlider.values[1])

            addView(
                minText,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.START
                })
            addView(
                maxText,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.END
                })
        })

        val surfaceLabel = TextView(context).apply {
            text = context.getString(R.string.surface_filter_label)
        }
        linearLayout.addView(surfaceLabel)

        val surfaceRangeSlider = RangeSlider(context).apply {
            id = SURFACE_ID
            valueFrom = minSurface
            valueTo = maxSurface
            setValues(minSurface, maxSurface)
        }
        linearLayout.addView(surfaceRangeSlider)

        linearLayout.addView(FrameLayout(context).apply {
            val minText = TextView(context).apply {
                setTextColor(Color.BLACK)
            }
            val maxText = TextView(context).apply {
                setTextColor(Color.BLACK)
            }
            // the listener
            surfaceRangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { rangeSlider, _, _ ->
                minText.text = rangeSlider.values[0].roundToInt().toString()
                maxText.text = rangeSlider.values[1].roundToInt().toString()
            })
            // the initial value
            minText.text = surfaceRangeSlider.values[0].roundToInt().toString()
            maxText.text = surfaceRangeSlider.values[1].roundToInt().toString()

            addView(
                minText,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.START
                })
            addView(
                maxText,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.END
                })
        })

        val roomsLabel = TextView(context).apply {
            text = context.getString(R.string.rooms_filter_label)
        }
        linearLayout.addView(roomsLabel)

        val roomsRangeSlider = RangeSlider(context).apply {
            valueFrom = minRooms
            valueTo = maxRooms
            setValues(minRooms, maxRooms)
        }
        linearLayout.addView(roomsRangeSlider)

        linearLayout.addView(FrameLayout(context).apply {
            val minText = TextView(context).apply {
                setTextColor(Color.BLACK)
            }
            val maxText = TextView(context).apply {
                setTextColor(Color.BLACK)
            }
            // the listener
            roomsRangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { rangeSlider, _, _ ->
                minText.text = rangeSlider.values[0].roundToInt().toString()
                maxText.text = rangeSlider.values[1].roundToInt().toString()
            })
            // the initial value
            minText.text = roomsRangeSlider.values[0].roundToInt().toString()
            maxText.text = roomsRangeSlider.values[1].roundToInt().toString()

            addView(
                minText,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.START
                })
            addView(
                maxText,
                FrameLayout.LayoutParams(
                    ViewGroup.LayoutParams.WRAP_CONTENT,
                    ViewGroup.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.END
                })
        })

        builder.setView(linearLayout)

        builder.setPositiveButton("OK") { _, _ ->
            val selectedMinPrice = priceRangeSlider.values[0]
            val selectedMaxPrice = priceRangeSlider.values[1]
            val selectedMinSurface = surfaceRangeSlider.values[0]
            val selectedMaxSurface = surfaceRangeSlider.values[1]
            val selectedMinRooms = roomsRangeSlider.values[0]
            val selectedMaxRooms = roomsRangeSlider.values[1]
            onFilterSelected(
                selectedMinPrice,
                selectedMaxPrice,
                selectedMinSurface,
                selectedMaxSurface,
                selectedMinRooms,
                selectedMaxRooms
            )
        }
        builder.setNegativeButton("Annuler", null)

        builder.create().show()
    }

    private fun onFilterSelected(
        minPrice: Float,
        maxPrice: Float,
        minSurface: Float,
        maxSurface: Float,
        minRooms: Float,
        maxRooms: Float
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredEstates = realEstateDao.getFilteredRealEstates(
                minPrice.toDouble(),
                maxPrice.toDouble(),
                minSurface.toDouble(),
                maxSurface.toDouble(),
                minRooms.toDouble(),
                maxRooms.toDouble()
            ).first()
            withContext(Dispatchers.Main) {
                onUpdateUI(filteredEstates)
            }
        }
    }

    fun fetchPriceRangeAndShowDialog() {
        CoroutineScope(Dispatchers.IO).launch {
            val maxPriceValue = realEstateDao.getMaxPrice()?.toFloat() ?: 1000000f
            val maxSurface = realEstateDao.getMaxSurface()?.toFloat() ?: 1000f
            val maxRooms = realEstateDao.getMaxRooms()?.toFloat() ?: 6f

            withContext(Dispatchers.Main) {
                showFilterDialog(
                    minPrice = 0f,
                    maxPrice = maxPriceValue + 1.0f,
                    minSurface = 0f,
                    maxSurface = maxSurface + 1.0f,
                    minRooms = 1f,
                    maxRooms = maxRooms + 1.0f,
                )
            }
        }
    }


}





