package com.openclassrooms.realestatemanager.adapters

import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.database.tables.Media

class EstateDetailsAdapter(private var mediaItems: MutableList<Media>) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onItemClicked: ((Media) -> Unit)? = null
    private var onAddButtonClicked: (() -> Unit)? = null


    companion object {
        const val REQUEST_CODE_PICK_MEDIA = 1001
        const val VIEW_TYPE_ITEM = 0
        const val VIEW_TYPE_ADD = 1
    }

    fun setOnItemClickedListener(listener: (Media) -> Unit) {
        onItemClicked = listener
    }

    fun setOnAddButtonClickedListener(listener: () -> Unit) {
        onAddButtonClicked = listener
    }

    fun addMediaItem(newMedia: Media) {
        val lastIndex = mediaItems.size
        mediaItems.add(newMedia)
        notifyItemInserted(lastIndex)
        Log.d("EstateDetailsAdapter", "Nouvel élément ajouté à la position $lastIndex")
    }



    override fun getItemViewType(position: Int): Int {
        return if (position < mediaItems.size) VIEW_TYPE_ITEM else VIEW_TYPE_ADD
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == VIEW_TYPE_ITEM) {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.image_item, parent, false)
            ViewHolder(view)
        } else {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.add_button_item, parent, false)
            AddButtonViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is ViewHolder -> {
                val mediaItem = mediaItems[position]
                with(holder) {
                    if (mediaItem.type == "image") {
                        imageView.visibility = View.VISIBLE
                        videoView.visibility = View.GONE
                        Glide.with(imageView.context).load(mediaItem.uri).into(imageView)
                    } else if (mediaItem.type == "video") {
                        imageView.visibility = View.GONE
                        videoView.visibility = View.VISIBLE
                        videoView.setVideoURI(Uri.parse(mediaItem.uri))
                        videoView.seekTo(1)
                    }
                    itemView.setOnClickListener {
                        onItemClicked?.invoke(mediaItem)
                    }
                }
            }
            is AddButtonViewHolder -> {
                holder.addButton.setOnClickListener {
                    onAddButtonClicked?.invoke()
                }
            }
        }
    }

    fun updateMediaItems(newMediaItems: List<Media>) {
        mediaItems = newMediaItems.toMutableList()
        notifyDataSetChanged()
    }


    override fun getItemCount() = mediaItems.size + 1

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.thumbnail_image)
        val videoView: VideoView = view.findViewById(R.id.thumbnail_video)
    }

    class AddButtonViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val addButton: ImageView = view.findViewById(R.id.add_button)

        init {
            addButton.setOnClickListener {
                Log.d("EstateDetailsAdapter", "Le bouton + a été cliqué")
            }
        }
    }

}
