package com.openclassrooms.realestatemanager.estate_manager.logic

import RealEstateManagerViewModel
import android.content.Context
import android.location.Address
import android.location.Geocoder
import android.net.Uri
import android.view.LayoutInflater
import android.view.Surface
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.RadioButton
import android.widget.RadioGroup
import androidx.appcompat.app.AlertDialog
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import java.util.Locale

class AddNewEstate(
) {
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
        val editName = dialogLayout.findViewById<EditText>(R.id.editName)
        val editDescription = dialogLayout.findViewById<EditText>(R.id.editDescription)
        val editAddress = dialogLayout.findViewById<EditText>(R.id.editAddress)
        val editPrice = dialogLayout.findViewById<EditText>(R.id.editPrice)
        val radioGroupSended: RadioGroup = dialogLayout.findViewById(R.id.radioGroupSended)
        val radioButtonOnSale: RadioButton = dialogLayout.findViewById(R.id.radioButtonOnSale)
        val buttonAddEstate = dialogLayout.findViewById<Button>(R.id.buttonAddEstate)
        val editSurface = dialogLayout.findViewById<EditText>(R.id.editSurface)
        val editRooms = dialogLayout.findViewById<EditText>(R.id.editRooms)

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
            val img = imageUri ?: realEstate?.img
            val name = editName.text.toString()
            val description = editDescription.text.toString()
            val address = editAddress.text.toString()
            val price = editPrice.text.toString().toIntOrNull() ?: 0
            val surfaceText = editSurface.text.toString()
            val surface = if (surfaceText.isNotEmpty()) {
                try {
                    surfaceText.toDouble()
                } catch (e: NumberFormatException) {
                    0.0
                }
            } else {
                0.0
            }

            val rooms = editRooms.text.toString().toIntOrNull() ?: 0
            val isOnSale = radioButtonOnSale.isChecked

            val geocoder = Geocoder(context, Locale.getDefault())

            GlobalScope.launch(Dispatchers.Main) {
                createNewEstate(
                    img = img.toString(),
                    name = name,
                    description = description,
                    address = address,
                    price = price,
                    surface = surface,
                    rooms = rooms,
                    isOnSale = isOnSale,
                    realEstate = realEstate,
                    viewModel = viewModel,
                    dialog = dialog,
                    getFromLocationName = {
                        geocoder.getFromLocationName(address, 1)
                    }
                )
            }
        }


        builder.setNegativeButton(context.getString(R.string.cancel)) { dialogInterface, _ ->
            dialog.dismiss()
        }
        dialog.show()
    }

    internal fun createNewEstate(
        img: String?,
        name: String,
        description: String,
        address: String,
        price: Int,
        surface: Double,
        rooms: Int,
        getFromLocationName: (String) -> List<Address>?,
        isOnSale: Boolean,
        realEstate: RealEstate?,
        viewModel: RealEstateManagerViewModel,
        dialog: AlertDialog,
    ) {
        if (img != null && name.isNotBlank() && description.isNotBlank() && address.isNotBlank() && price > 0) {
            try {
                val addressList: List<Address>? = getFromLocationName(address)
                if (addressList != null && !addressList.isEmpty()) {
                    val returnedAddress: Address = addressList[0]
                    val latitude = returnedAddress.latitude
                    val longitude = returnedAddress.longitude

                    val newEstate = RealEstate(
                        0,
                        img = img,
                        name = name,
                        description = description,
                        address = address,
                        price = price,
                        sended = isOnSale,
                        latitude = latitude,
                        longitude = longitude,
                        surface = surface,
                        rooms = rooms
                    )

                    if (realEstate == null) {
                        viewModel.addNewRealEstate(newEstate)
                    } else {
                        viewModel.editRealEstate(
                            realEstate.copy(
                                img = img.toString(),
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
            } catch (e: Exception) {
                print("Erreur lors du géocodage: ${e.message}")
            }
        } else {
            print("error")
        }
    }
}



