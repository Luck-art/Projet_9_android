package com.openclassrooms.realestatemanager.estate_manager.logic

import RealEstateManagerViewModel
import android.content.Context
import android.view.LayoutInflater
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AlertDialog
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.dao.RealEstateDao
import com.openclassrooms.realestatemanager.database.tables.RealEstate
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AddNewEstate(
    private val context: Context,
    private val realEstateDao: RealEstateDao,
    private val viewModel: RealEstateManagerViewModel
) {
    fun showAddPropertyDialog() {
        val builder = AlertDialog.Builder(context)
        val inflater = LayoutInflater.from(context)
        val dialogLayout = inflater.inflate(R.layout.add_new_estate, null)

        val editImage = dialogLayout.findViewById<EditText>(R.id.editImageUrl)
        val editName = dialogLayout.findViewById<EditText>(R.id.editName)
        val editDescription = dialogLayout.findViewById<EditText>(R.id.editDescription)
        val editPrice = dialogLayout.findViewById<EditText>(R.id.editPrice)
        val buttonAddEstate = dialogLayout.findViewById<Button>(R.id.buttonAddEstate)

        builder.setView(dialogLayout)

        val dialog = builder.create()

        buttonAddEstate.setOnClickListener {
            val img = editImage.text.toString()
            val name = editName.text.toString()
            val description = editDescription.text.toString()
            val price = editPrice.text.toString().toIntOrNull() ?: 0

            if(img.isNotBlank() && name.isNotBlank() && description.isNotBlank() && price > 0) {
                CoroutineScope(Dispatchers.IO).launch {
                    val newEstate = RealEstate(0, img, name, description, price)
                    viewModel.addNewRealEstate(newEstate)
                }

                dialog.dismiss()
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



