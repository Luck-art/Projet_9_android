package com.openclassrooms.realestatemanager.estate_manager.logic

import RealEstateManagerViewModel
import android.content.Context
import android.location.Geocoder
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import com.openclassrooms.realestatemanager.estate_manager.RealEstateManagerActivity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import java.util.Locale

class AddNewEstate(
) {
    fun showAddPropertyDialog(
        viewModel: RealEstateManagerViewModel,
        context: Context,
        realEstate: RealEstate?
    ) {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogLayout = inflater.inflate(R.layout.add_new_estate, null)

        val editImage = dialogLayout.findViewById<EditText>(R.id.editImageUrl)
        val editName = dialogLayout.findViewById<EditText>(R.id.editName)
        val editDescription = dialogLayout.findViewById<EditText>(R.id.editDescription)
        val editAddress = dialogLayout.findViewById<EditText>(R.id.editAddress)
        val editPrice = dialogLayout.findViewById<EditText>(R.id.editPrice)
        val radioGroupSended: RadioGroup = dialogLayout.findViewById(R.id.radioGroupSended)
        val radioButtonOnSale: RadioButton = dialogLayout.findViewById(R.id.radioButtonOnSale)
        val buttonAddEstate = dialogLayout.findViewById<Button>(R.id.buttonAddEstate)

        realEstate?.img?.let {
            editImage.setText(it)
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
        realEstate?.sended?.let { isOnSale ->
            if (isOnSale) {
                radioButtonOnSale.isChecked = true
            } else {
                dialogLayout.findViewById<RadioButton>(R.id.radioButtonSold).isChecked = true
            }
        }

        builder.setView(dialogLayout)

        val dialog = builder.create()

        buttonAddEstate.setOnClickListener {
            val img = editImage.text.toString()
            val name = editName.text.toString()
            val description = editDescription.text.toString()
            val address = editAddress.text.toString()
            val price = editPrice.text.toString().toIntOrNull() ?: 0
            val isOnSale = radioButtonOnSale.isChecked

            if (img.isNotBlank() && name.isNotBlank() && description.isNotBlank() && address.isNotBlank() && price > 0) {
                try {
                    val geocoder = Geocoder(context, Locale.getDefault())

                    GlobalScope.launch(Dispatchers.IO) {
                        //
                        val addressList: List<android.location.Address>? = geocoder.getFromLocationName(address, 1)
                        withContext(Dispatchers.Main) {
                            if (addressList != null && !addressList.isEmpty()) {
                                val returnedAddress: android.location.Address = addressList[0]
                                val latitude = returnedAddress.latitude
                                val longitude = returnedAddress.longitude

                                val newEstate = RealEstate(
                                    0,
                                    img,
                                    name,
                                    description,
                                    address,
                                    price,
                                    sended = isOnSale,
                                    latitude = latitude,
                                    longitude = longitude
                                )

                                if (realEstate == null) {
                                    viewModel.addNewRealEstate(newEstate)
                                } else {
                                    viewModel.editRealEstate(
                                        realEstate.copy(
                                            img = img,
                                            name = name,
                                            description = description,
                                            address = address,
                                            price = price,
                                            sended = isOnSale,
                                        )
                                    )
                                }
                                dialog.dismiss()
                            } else {
                                print("Adresse non trouvée")
                            }
                        }
                    }
                } catch (e: Exception) {
                    print("Erreur lors du géocodage: ${e.message}")
                }
            } else {
                print("error")
            }
        }


        builder.setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }
}



