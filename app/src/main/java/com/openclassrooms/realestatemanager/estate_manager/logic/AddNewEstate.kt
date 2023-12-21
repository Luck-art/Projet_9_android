package com.openclassrooms.realestatemanager.estate_manager.logic

import RealEstateManagerViewModel
import android.annotation.SuppressLint
import android.app.DatePickerDialog
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.content.ContentProviderCompat.requireContext
import com.bumptech.glide.Glide
import com.google.android.material.internal.CheckableGroup
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class AddNewEstate(
) {

    private fun showDatePickerDialog(editText: EditText) {
        val calendar = Calendar.getInstance()
        val year = calendar.get(Calendar.YEAR)
        val month = calendar.get(Calendar.MONTH)
        val day = calendar.get(Calendar.DAY_OF_MONTH)

        val datePickerDialog = DatePickerDialog(
            editText.context,
            { _, selectedYear, selectedMonth, selectedDay ->
                val selectedCalendar = Calendar.getInstance()
                selectedCalendar.set(selectedYear, selectedMonth, selectedDay)

                val currentCalendar = Calendar.getInstance()
                selectedCalendar.set(Calendar.HOUR_OF_DAY, currentCalendar.get(Calendar.HOUR_OF_DAY))
                selectedCalendar.set(Calendar.MINUTE, currentCalendar.get(Calendar.MINUTE))
                selectedCalendar.set(Calendar.SECOND, currentCalendar.get(Calendar.SECOND))

                val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
                val formattedDate = dateFormat.format(selectedCalendar.time)
                editText.setText(formattedDate)
            },
            year, month, day
        )
        datePickerDialog.show()

    }


    @SuppressLint("CutPasteId")
    fun showAddPropertyDialog(
        viewModel: RealEstateManagerViewModel,
        context: Context,
        realEstate: RealEstate?,
        pickPhoto: suspend () -> Uri?,
    ) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogLayout = inflater.inflate(R.layout.add_new_estate, null)

        val photoContainer = dialogLayout.findViewById<View>(R.id.photoContainer)
        val selectedPhoto = dialogLayout.findViewById<ImageView>(R.id.selectedPhoto)
        val checkBoxHouse: CheckBox = dialogLayout.findViewById(R.id.checkBoxHouse)
        val checkBoxLoft: CheckBox = dialogLayout.findViewById(R.id.checkBoxLoft)
        val checkBoxApartment: CheckBox = dialogLayout.findViewById(R.id.checkBoxApartment)
        val selectedEstateTypes = mutableListOf<String>()
        val editName = dialogLayout.findViewById<EditText>(R.id.editName)
        val editDescription = dialogLayout.findViewById<EditText>(R.id.editDescription)
        val editAddress = dialogLayout.findViewById<EditText>(R.id.editAddress)
        val editPrice = dialogLayout.findViewById<EditText>(R.id.editPrice)
        val checkBoxSchool: CheckBox = dialogLayout.findViewById(R.id.checkBoxSchool)
        val checkBoxShops: CheckBox = dialogLayout.findViewById(R.id.checkBoxShops)
        val checkBoxRestaurants: CheckBox = dialogLayout.findViewById(R.id.checkBoxRestaurants)
        val checkBoxGym: CheckBox = dialogLayout.findViewById(R.id.checkBoxGym)
        val checkBoxFastFood: CheckBox = dialogLayout.findViewById(R.id.checkBoxFastFood)
        val checkBoxPark: CheckBox = dialogLayout.findViewById(R.id.checkBoxPark)
        val selectedPointsOfInterest = mutableListOf<String>()
        val editEstateAgent = dialogLayout.findViewById<EditText>(R.id.estateAgent)
        val radioGroupSended: RadioGroup = dialogLayout.findViewById(R.id.radioGroupSended)
        val radioButtonOnSale: RadioButton = dialogLayout.findViewById(R.id.radioButtonOnSale)
        val buttonAddEstate = dialogLayout.findViewById<Button>(R.id.buttonAddEstate)
        val editSurface = dialogLayout.findViewById<EditText>(R.id.editSurface)
        val editDateSold = dialogLayout.findViewById<EditText>(R.id.editTextSoldDate)
        val editDateSale = dialogLayout.findViewById<EditText>(R.id.editTextSaleDate)
        val editRooms = dialogLayout.findViewById<EditText>(R.id.editRooms)

       editDateSale.isFocusable = false
        editDateSale.isClickable = true

        editDateSale.setOnClickListener {
            showDatePickerDialog(editDateSale)
        }

        editDateSold.isFocusable = false
        editDateSold.isClickable = true

        editDateSold.setOnClickListener {
            showDatePickerDialog(editDateSold)
        }

        var imageUri: Uri? = null

        realEstate?.img?.let {
            Glide.with(context)
                .load(it)
                .into(selectedPhoto)
        }
        photoContainer.setOnClickListener {
            GlobalScope.launch(Dispatchers.Main) {
                imageUri = pickPhoto()
                imageUri?.let {
                    Glide.with(context)
                        .load(it)
                        .into(selectedPhoto)
                }
            }
        }
        realEstate?.estate_type?.let { type ->
            when (type) {
                "House" -> checkBoxHouse.isChecked = true
                "Loft" -> checkBoxLoft.isChecked = true
                "Apartment" -> checkBoxApartment.isChecked = true
            }
        }
        realEstate?.point_interest?.forEach { point ->
            when (point) {
                "School" -> checkBoxSchool.isChecked = true
                "Shops" -> checkBoxShops.isChecked = true
                "Restaurant" -> checkBoxRestaurants.isChecked = true
                "Gymnast" -> checkBoxGym.isChecked = true
                "Fast food" -> checkBoxFastFood.isChecked = true
                "Park" -> checkBoxPark.isChecked = true
            }
        }
        realEstate?.name?.let {
            editName.setText(it)
        }
        realEstate?.description?.let {
            editDescription.setText(it)
        }
        realEstate?.address?.let {
            editAddress.setText(it)
        }
        realEstate?.price?.let {
            editPrice.setText(it.toString())
        }
        realEstate?.surface?.let {
            editSurface.setText(it.toString())
        }
        realEstate?.rooms?.let {
            editRooms.setText(it.toString())
        }
        realEstate?.estate_agent?.let {
            editEstateAgent.setText(it.toString())
        }
        realEstate?.sended?.let { isOnSale ->
            if (isOnSale) {
                radioButtonOnSale.isChecked = true
            } else {
                dialogLayout.findViewById<RadioButton>(R.id.radioButtonSold).isChecked = true
            }
        }

        val editTextSaleDate = dialogLayout.findViewById<EditText>(R.id.editTextSaleDate)
        val editTextSoldDate = dialogLayout.findViewById<EditText>(R.id.editTextSoldDate)

        radioGroupSended.setOnCheckedChangeListener { group, checkedId ->
            when (checkedId) {
                R.id.radioButtonOnSale -> {
                    editTextSaleDate.visibility = View.VISIBLE
                    editTextSoldDate.visibility = View.GONE
                }

                R.id.radioButtonSold -> {
                    editTextSaleDate.visibility = View.GONE
                    editTextSoldDate.visibility = View.VISIBLE
                }
            }
        }

        builder.setView(dialogLayout)

        val dialog = builder.create()

        buttonAddEstate.setOnClickListener {
            val img = imageUri ?: realEstate?.img
            val name = editName.text.toString()
            val description = editDescription.text.toString()
            val address = editAddress.text.toString()
            val price = editPrice.text.toString().toIntOrNull() ?: 0
            val surfaceText = editSurface.text.toString()
            val dateFormat = SimpleDateFormat("yyyy/MM/dd HH:mm:ss", Locale.getDefault())
            val dateSale: Date? = try {
                dateFormat.parse(editDateSale.text.toString())
            } catch (e: ParseException) {
                null
            }

            val dateSold: Date? = try {
                dateFormat.parse(editDateSold.text.toString())
            } catch (e: ParseException) {
                null
            }
            val surface = if (surfaceText.isNotEmpty()) {
                try {
                    surfaceText.toDouble()
                } catch (e: NumberFormatException) {
                    0.0
                }
            } else {
                0.0
            }
            val estateAgent = editEstateAgent.text.toString()
            val rooms = editRooms.text.toString().toIntOrNull() ?: 0
            val isOnSale = radioButtonOnSale.isChecked
            val geocoder = Geocoder(context, Locale.getDefault())

            GlobalScope.launch(Dispatchers.Main) {
                if (dateSold != null || dateSale != null) {
                    createNewEstate(
                        img = img.toString(),
                        estate_type = selectedEstateTypes,
                        pointsOfInterest = selectedPointsOfInterest,
                        name = name,
                        description = description,
                        address = address,
                        price = price,
                        surface = surface,
                        rooms = rooms,
                        estateAgent = estateAgent,
                        isOnSale = isOnSale,
                        realEstate = realEstate,
                        viewModel = viewModel,
                        dialog = dialog,
                        dateSold = dateSold,
                        dateSale = dateSale,
                        getFromLocationName = {
                            geocoder.getFromLocationName(address, 1)
                        },
                        context = context
                    )
                    dialog.dismiss()
                }
            }
        }

        checkBoxHouse.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedEstateTypes.clear()
                selectedEstateTypes.add("House")
                checkBoxLoft.isChecked = false
                checkBoxApartment.isChecked = false
            } else {
                selectedEstateTypes.remove("House")
            }
        }

        checkBoxLoft.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedEstateTypes.clear()
                selectedEstateTypes.add("Loft")
                checkBoxHouse.isChecked = false
                checkBoxApartment.isChecked = false
            } else {
                selectedEstateTypes.remove("Loft")
            }
        }

        checkBoxApartment.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedEstateTypes.clear()
                selectedEstateTypes.add("Apartment")
                checkBoxHouse.isChecked = false
                checkBoxLoft.isChecked = false
            } else {
                selectedEstateTypes.remove("Apartment")
            }
        }

        checkBoxSchool.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPointsOfInterest.add("School")
            } else {
                selectedPointsOfInterest.remove("School")
            }
        }

        checkBoxShops.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPointsOfInterest.add("Shop")
            } else {
                selectedPointsOfInterest.remove("Shop")
            }
        }

        checkBoxGym.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPointsOfInterest.add("Gymnast")
            } else {
                selectedPointsOfInterest.remove("Gymnast")
            }
        }

        checkBoxRestaurants.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPointsOfInterest.add("Restaurant")
            } else {
                selectedPointsOfInterest.remove("Restaurant")
            }
        }
        checkBoxPark.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPointsOfInterest.add("Park")
            } else {
                selectedPointsOfInterest.remove("Park")
            }
        }

        checkBoxFastFood.setOnCheckedChangeListener { _, isChecked ->
            if (isChecked) {
                selectedPointsOfInterest.add("Fast food")
            } else {
                selectedPointsOfInterest.remove("Fast food")
            }
        }



        builder.setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    internal fun createNewEstate(
        img: String?,
        estate_type: List<String>,
        pointsOfInterest: List<String>,
        name: String,
        description: String,
        address: String,
        price: Int,
        surface: Double,
        rooms: Int,
        estateAgent: String,
        dateSale: Date?,
        dateSold: Date?,
        getFromLocationName: (String) -> List<Address>?,
        isOnSale: Boolean,
        realEstate: RealEstate?,
        viewModel: RealEstateManagerViewModel,
        dialog: AlertDialog,
        context: Context
    ) {
        if (img != null && name.isNotBlank() && description.isNotBlank() && address.isNotBlank() && price > 0) {
            try {
                val addressList: List<Address>? = getFromLocationName(address)
                if (addressList != null && !addressList.isEmpty()) {
                    val returnedAddress: Address = addressList[0]
                    val latitude = returnedAddress.latitude
                    val longitude = returnedAddress.longitude

                    val newEstate = RealEstate(
                        img = img,
                        estate_type = estate_type.joinToString(", "),
                        name = name,
                        description = description,
                        address = address,
                        price = price,
                        sended = isOnSale,
                        latitude = latitude,
                        longitude = longitude,
                        surface = surface,
                        rooms = rooms,
                        estate_agent = estateAgent,
                        date_sale = dateSale,
                        date_sold = dateSold,
                        point_interest = pointsOfInterest
                    )

                    if (realEstate == null) {
                        viewModel.addNewRealEstate(newEstate)
                    } else {
                        val updatedRealEstate = realEstate.copy(
                            img = img.toString(),
                            estate_type = estate_type.joinToString(", "),
                            name = name,
                            description = description,
                            address = address,
                            price = price,
                            sended = isOnSale,
                            latitude = latitude,
                            longitude = longitude,
                            surface = surface,
                            rooms = rooms,
                            estate_agent = estateAgent,
                            date_sale = dateSale,
                            date_sold = dateSold,
                            point_interest = pointsOfInterest
                        )
                        Log.d("Update", "Updated Real Estate: $updatedRealEstate")
                        viewModel.editRealEstate(updatedRealEstate)
                    }

                    dialog.dismiss()
                    Toast.makeText(context, R.string.toast_property_created, Toast.LENGTH_SHORT).show()
                } else {
                    print("Adresse non trouvée")
                }
            } catch (e: Exception) {
                print("Erreur lors du géocodage: ${e.message}")
            }
        } else {
            print("error")
        }
    }
}



