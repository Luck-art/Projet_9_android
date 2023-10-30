package com.openclassrooms.realestatemanager.adapters

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.request.RequestListener
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.tables.RealEstate

class RealEstateManagerAdapter(private val dataSet: List<RealEstate>,private val onItemClicked: (RealEstate, Boolean) -> Unit, private var isInEditMode: Boolean = false) :
    RecyclerView.Adapter<RealEstateManagerAdapter.ViewHolder>() {

    private var filteredDataSet: List<RealEstate> = dataSet

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
        val item = filteredDataSet[position]

        Glide.with(holder.estateImageView.context)
            .load(item.img)
            .into(holder.estateImageView)


        holder.itemView.setOnClickListener {
           onItemClicked(item, isInEditMode)
        }
        holder.estateTextView.text = item.name
        holder.estateDesciptionView.text = item.description
        holder.estatePriceView.text = item.price.toString()

        Glide.with(holder.estateImageView.context)
            .load(item.img)
            .listener(object : RequestListener<Drawable> {
                override fun onLoadFailed(
                    e: GlideException?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    isFirstResource: Boolean
                ): Boolean {
                    Log.e("GlideError", "Load failed", e)
                    return false
                }

                override fun onResourceReady(
                    resource: Drawable?,
                    model: Any?,
                    target: com.bumptech.glide.request.target.Target<Drawable>?,
                    dataSource: DataSource?,
                    isFirstResource: Boolean
                ): Boolean {
                    return false
                }
            })

            .into(holder.estateImageView)


    }

    // Search filter

    fun filterItems(text: String) {
        filteredDataSet = if (text.isEmpty()) {
            dataSet
        } else {
            dataSet.filter { it.name.contains(text, ignoreCase = true) }
        }
        notifyDataSetChanged()
    }

    fun setEditMode(isInEditMode: Boolean) {
        this.isInEditMode = isInEditMode
        notifyDataSetChanged()
    }


    override fun getItemCount() = filteredDataSet.size
}