package com.openclassrooms.realestatemanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.tables.RealEstate

class RealEstateManagerAdapter(private val dataSet: List<RealEstate>) :
    RecyclerView.Adapter<RealEstateManagerAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val estateImageView: ImageView = view.findViewById(R.id.estate_img)
        val estateTextView: TextView = view.findViewById(R.id.estate_name)
        val estateDesciptionView: TextView = view.findViewById(R.id.estate_description)
        val estatePriceView: TextView = view.findViewById(R.id.estate_price)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.real_estate_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = dataSet[position]

        Glide.with(holder.estateImageView.context)
            .load(item.img)
            .into(holder.estateImageView)

        holder.estateTextView.text = item.name
        holder.estateDesciptionView.text = item.description
        holder.estatePriceView.text = item.price.toString()
    }

    override fun getItemCount() = dataSet.size
}