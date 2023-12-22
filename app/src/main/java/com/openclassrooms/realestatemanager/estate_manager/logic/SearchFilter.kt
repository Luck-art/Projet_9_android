package com.openclassrooms.realestatemanager.estate_manager.logic

import android.content.Context
import android.graphics.Color
import android.icu.text.NumberFormat
import android.icu.util.Currency
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.ScrollView
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
    private lateinit var apartmentCheckBox: CheckBox
    private lateinit var loftCheckBox: CheckBox
    private lateinit var houseCheckBox: CheckBox
    private lateinit var schoolCheckBox: CheckBox
    private lateinit var parkCheckBox: CheckBox
    private lateinit var restaurantCheckBox: CheckBox
    private lateinit var gymnastCheckBox: CheckBox
    private lateinit var shopCheckBox: CheckBox
    private lateinit var fastFoodCheckBox: CheckBox


    companion object {
        val PRICE_ID = View.generateViewId()
        val SURFACE_ID = View.generateViewId()
    }

    private var soldSelected = false
    private var availableSelected = false
    private var apartmentSelected = false
    private var loftSelected = false
    private var houseSelected = false
    private var schoolSelected = false
    private var parkSelected = false
    private var restaurantSelected = false
    private var gymnastSelected = false
    private var shopSelected = false
    private var fastFoodSelected = false

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

        val scrollView = ScrollView(context).apply {
            layoutParams = ViewGroup.LayoutParams(
                ViewGroup.LayoutParams.MATCH_PARENT,
                ViewGroup.LayoutParams.WRAP_CONTENT
            )
        }

        val linearLayout = LinearLayout(context).apply {
            orientation = LinearLayout.VERTICAL
            layoutParams = LinearLayout.LayoutParams(
                LinearLayout.LayoutParams.MATCH_PARENT,
                LinearLayout.LayoutParams.WRAP_CONTENT
            )
            val padding = context.resources.getDimensionPixelSize(R.dimen.default_padding)
            setPadding(padding, padding, padding, padding)
        }

        scrollView.addView(linearLayout)

        // sale filter

        val saleLabel = TextView(context).apply {
            text = context.getString(R.string.sale_filter_label)
        }
        linearLayout.addView(saleLabel)

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


        // type filter


        val typeLabel = TextView(context).apply {
            text = context.getString(R.string.type_filter_label)
        }
        linearLayout.addView(typeLabel)

        apartmentCheckBox = CheckBox(context).apply {
            text = "Apartment"
            isChecked = apartmentSelected
            setOnCheckedChangeListener { _, isChecked -> apartmentSelected = isChecked }
        }
        linearLayout.addView(apartmentCheckBox)

        loftCheckBox = CheckBox(context).apply {
            text = "Loft"
            isChecked = loftSelected
            setOnCheckedChangeListener { _, isChecked -> loftSelected = isChecked }
        }
        linearLayout.addView(loftCheckBox)

        houseCheckBox = CheckBox(context).apply {
            text = "House"
            isChecked = houseSelected
            setOnCheckedChangeListener { _, isChecked -> houseSelected = isChecked }
        }
        linearLayout.addView(houseCheckBox)


        // interest point filter


        val interestLabel = TextView(context).apply {
            text = context.getString(R.string.interest_filter_label)
        }
        linearLayout.addView(interestLabel)

        schoolCheckBox = CheckBox(context).apply {
            text = "School"
            isChecked = schoolSelected
            setOnCheckedChangeListener { _, isChecked -> schoolSelected = isChecked }
        }
        linearLayout.addView(schoolCheckBox)

        parkCheckBox = CheckBox(context).apply {
            text = "Park"
            isChecked = parkSelected
            setOnCheckedChangeListener { _, isChecked -> parkSelected = isChecked }
        }
        linearLayout.addView(parkCheckBox)

        restaurantCheckBox = CheckBox(context).apply {
            text = "Restaurant"
            isChecked = schoolSelected
            setOnCheckedChangeListener { _, isChecked -> schoolSelected = isChecked }
        }
        linearLayout.addView(restaurantCheckBox)

        gymnastCheckBox = CheckBox(context).apply {
            text = "Sport"
            isChecked = gymnastSelected
            setOnCheckedChangeListener { _, isChecked -> gymnastSelected = isChecked }
        }
        linearLayout.addView(gymnastCheckBox)

        shopCheckBox = CheckBox(context).apply {
            text = "Shop"
            isChecked = shopSelected
            setOnCheckedChangeListener { _, isChecked -> shopSelected = isChecked }
        }
        linearLayout.addView(shopCheckBox)

        fastFoodCheckBox = CheckBox(context).apply {
            text = "Fast food"
            isChecked = fastFoodSelected
            setOnCheckedChangeListener { _, isChecked -> fastFoodSelected = isChecked }
        }
        linearLayout.addView(fastFoodCheckBox)


        // price filter


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
            currency = Currency.getInstance("USD")
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


        // surface filter



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



        // rooms filter


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

        builder.setView(scrollView)

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

           val estateType = when {
                apartmentSelected && availableSelected -> "Appartement"
                loftSelected && availableSelected-> "Loft"
                houseSelected && availableSelected-> "Maison"
                else -> null
            }

            val selectedPointsOfInterest = mutableListOf<String>()
            if (schoolSelected) selectedPointsOfInterest.add("School")
            if (parkSelected) selectedPointsOfInterest.add("Park")
            if (restaurantSelected) selectedPointsOfInterest.add("Restaurant")
            if (gymnastSelected) selectedPointsOfInterest.add("Sport")
            if (shopSelected) selectedPointsOfInterest.add("Shop")
            if (fastFoodSelected) selectedPointsOfInterest.add("Fast food")


            Log.d("SearchFilter", "selectedMinPrice: $selectedMinPrice")
            Log.d("SearchFilter", "selectedMaxPrice: $selectedMaxPrice")
            Log.d("SearchFilter", "selectedMinSurface: $selectedMinSurface")
            Log.d("SearchFilter", "selectedMaxSurface: $selectedMaxSurface")
            Log.d("SearchFilter", "selectedMinRooms: $selectedMinRooms")
            Log.d("SearchFilter", "selectedMaxRooms: $selectedMaxRooms")
            Log.d("SearchFilter", "estateStatusSelected: $estateStatusSelected")
            Log.d("SearchFilter", "estateType: $estateType")

            if (estateStatusSelected != null || estateType != null || selectedPointsOfInterest.isNotEmpty()) {
                onFilterSelected(
                    selectedMinPrice,
                    selectedMaxPrice,
                    selectedMinSurface,
                    selectedMaxSurface,
                    selectedMinRooms,
                    selectedMaxRooms,
                    estateStatusSelected,
                    estateType,
                    selectedPointsOfInterest
                )
            } else {
                fetchAllEstates(
                    selectedMinPrice,
                    selectedMaxPrice,
                    selectedMinSurface,
                    selectedMaxSurface,
                    selectedMinRooms,
                    selectedMaxRooms,
                    selectedPointsOfInterest
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
        maxRooms: Float,
        pointsOfInterest: List<String>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val estateType = when {
                apartmentSelected -> "Apartment"
                loftSelected -> "Loft"
                houseSelected -> "House"
                else -> null
            }

            val allEstates = realEstateDao.getFilteredRealEstates(
                minPrice.toDouble(),
                maxPrice.toDouble(),
                minSurface.toDouble(),
                maxSurface.toDouble(),
                minRooms.toDouble(),
                maxRooms.toDouble(),
                null,
                estateType,
                null,
                null,
                null,
                null,
                null,
                null
            ).first()

            val filteredEstates = if (pointsOfInterest.isNotEmpty()) {
                allEstates.filter { estate ->
                    pointsOfInterest.any { interest ->
                        estate.point_interest.contains(interest)
                    }
                }
            } else {
                allEstates
            }

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
        estateStatus: Boolean?,
        estateType: String?,
        pointsOfInterest: List<String>
    ) {
        CoroutineScope(Dispatchers.IO).launch {
            val filteredEstates = realEstateDao.getFilteredRealEstates(
                minPrice.toDouble(),
                maxPrice.toDouble(),
                minSurface.toDouble(),
                maxSurface.toDouble(),
                minRooms.toDouble(),
                maxRooms.toDouble(),
                estateStatus,
                estateType,
                if (pointsOfInterest.contains("School")) "School" else null,
                if (pointsOfInterest.contains("Park")) "Park" else null,
                if (pointsOfInterest.contains("Restaurant")) "Restaurant" else null,
                if (pointsOfInterest.contains("Sport")) "Sport" else null,
                if (pointsOfInterest.contains("Shop")) "Shop" else null,
                if (pointsOfInterest.contains("Fast food")) "Fast food" else null,
            ).first()

            val filteredEstatesResult = if (pointsOfInterest.isNotEmpty()) {
                filteredEstates.filter { estate ->
                    pointsOfInterest.any { interest ->
                        estate.point_interest.contains(interest)
                    }
                }
            } else {
                filteredEstates
            }

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