package com.openclassrooms.realestatemanager.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.RealEstateListModel

class RealEstateListAdapter(private val dataSet: RealEstateListModel?) :
    RecyclerView.Adapter<RealEstateListAdapter.ViewHolder>() {

    private val itemList = listOfNotNull(dataSet)

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.imageView)
        val nameTextView: TextView = view.findViewById(R.id.nameTextView)
        val descriptionTextView: TextView = view.findViewById(R.id.descriptionTextView)
        val priceTextView: TextView = view.findViewById(R.id.priceTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.real_estate_item, parent, false)

        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = itemList[position]

        Glide.with(holder.imageView.context)
            .load(item.imageUrl)
            .into(holder.imageView)

        holder.nameTextView.text = item.name
        holder.descriptionTextView.text = item.description
        holder.priceTextView.text = item.price.toString()
    }

    override fun getItemCount() = itemList.size
}
