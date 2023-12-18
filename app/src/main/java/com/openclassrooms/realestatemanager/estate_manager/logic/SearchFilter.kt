package com.openclassrooms.realestatemanager.estate_manager.logic

import android.content.Context
import android.graphics.Color
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
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

    private lateinit var soldCheckBox: CheckBox
    private lateinit var availableCheckBox: CheckBox

    companion object {
        val PRICE_ID = View.generateViewId()
        val SURFACE_ID = View.generateViewId()
    }

    private var soldSelected = false
    private var availableSelected = false

    fun showFilterDialog(
        minPrice: Float,
        maxPrice: Float,
        minSurface: Float,
        maxSurface: Float,
        minRooms: Float,
        maxRooms: Float,
        sended: Boolean
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

        soldCheckBox = CheckBox(context).apply {
            text = "Biens vendus"
            isChecked = soldSelected
            setOnCheckedChangeListener { _, isChecked -> soldSelected = isChecked }
        }
        linearLayout.addView(soldCheckBox)

        availableCheckBox = CheckBox(context).apply {
            text = "Biens en vente"
            isChecked = availableSelected
            setOnCheckedChangeListener { _, isChecked -> availableSelected = isChecked }
        }
        linearLayout.addView(availableCheckBox)

        soldSelected = soldCheckBox.isChecked
        availableSelected = availableCheckBox.isChecked

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
            priceRangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { rangeSlider, _, _ ->
                minText.text = priceFormatter.format(rangeSlider.values[0])
                maxText.text = priceFormatter.format(rangeSlider.values[1])
            })
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
            surfaceRangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { rangeSlider, _, _ ->
                minText.text = rangeSlider.values[0].roundToInt().toString()
                maxText.text = rangeSlider.values[1].roundToInt().toString()
            })
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
            roomsRangeSlider.addOnChangeListener(RangeSlider.OnChangeListener { rangeSlider, _, _ ->
                minText.text = rangeSlider.values[0].roundToInt().toString()
                maxText.text = rangeSlider.values[1].roundToInt().toString()
            })
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

            val estateStatusSelected: Boolean? = when {
                soldSelected && availableSelected -> null
                !soldSelected && !availableSelected -> null
                soldSelected -> true
                availableSelected -> false
                else -> null
            }

            if (estateStatusSelected != null) {
                onFilterSelected(
                    selectedMinPrice,
                    selectedMaxPrice,
                    selectedMinSurface,
                    selectedMaxSurface,
                    selectedMinRooms,
                    selectedMaxRooms,
                    estateStatusSelected
                )
            } else {

                fetchAllEstates(
                    selectedMinPrice,
                    selectedMaxPrice,
                    selectedMinSurface,
                    selectedMaxSurface,
                    selectedMinRooms,
                    selectedMaxRooms
                )
            }
        }



        builder.setNegativeButton("Annuler", null)

        builder.create().show()


    }

    private fun fetchAllEstates(
        minPrice: Float,
        maxPrice: Float,
        minSurface: Float,
        maxSurface: Float,
        minRooms: Float,
        maxRooms: Float
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val allEstates = realEstateDao.getFilteredRealEstates(
                minPrice.toDouble(),
                maxPrice.toDouble(),
                minSurface.toDouble(),
                maxSurface.toDouble(),
                minRooms.toDouble(),
                maxRooms.toDouble(),
                null
            ).first()
            withContext(Dispatchers.Main) {
                onUpdateUI(allEstates)
            }
        }
    }


    private fun onFilterSelected(
        minPrice: Float,
        maxPrice: Float,
        minSurface: Float,
        maxSurface: Float,
        minRooms: Float,
        maxRooms: Float,
        estateStatus: Boolean
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredEstates = realEstateDao.getFilteredRealEstates(
                minPrice.toDouble(),
                maxPrice.toDouble(),
                minSurface.toDouble(),
                maxSurface.toDouble(),
                minRooms.toDouble(),
                maxRooms.toDouble(),
                estateStatus
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

            val estateStatusSelected = true

            withContext(Dispatchers.Main) {
                showFilterDialog(
                    minPrice = 0f,
                    maxPrice = maxPriceValue + 1.0f,
                    minSurface = 0f,
                    maxSurface = maxSurface + 1.0f,
                    minRooms = 1f,
                    maxRooms = maxRooms + 1.0f,
                    sended = estateStatusSelected
                )
            }
        }
    }


    private fun getEstateStatusFromUI(): Boolean {
        return false
    }


}





