import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.openclassrooms.realestatemanager.R
import com.openclassrooms.realestatemanager.models.EstateDetailsModel

class EstateDetailsAdapter(
    private val mediaItems: List<EstateDetailsModel.MediaItem>,
    private val onItemClicked: (EstateDetailsModel.MediaItem) -> Unit
) : RecyclerView.Adapter<EstateDetailsAdapter.ViewHolder>() {

    interface OnItemClickListener {
        fun onItemClick(url: String)
    }

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val imageView: ImageView = view.findViewById(R.id.thumbnail_image)
        val videoView: VideoView = view.findViewById(R.id.thumbnail_video)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.image_item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mediaItem = mediaItems[position]
        holder.itemView.setOnClickListener {
            onItemClicked(mediaItem)
        }

        if (mediaItem.type == "image") {
            holder.imageView.visibility = View.VISIBLE
            holder.videoView.visibility = View.GONE
            Glide.with(holder.imageView.context)
                .load(mediaItem.url)
                .into(holder.imageView)
        } else if (mediaItem.type == "video") {
            holder.imageView.visibility = View.GONE
            holder.videoView.visibility = View.VISIBLE
            // Pour afficher une vignette, vous pouvez utiliser une bibliothèque tierce ou une logique spécifique à votre application
            // Note: Cette partie est un exemple simplifié, vous aurez besoin de déterminer comment obtenir des vignettes pour vos vidéos.
            val videoUri = Uri.parse(mediaItem.url)
            holder.videoView.setVideoURI(videoUri)
            holder.videoView.seekTo(1)  // Cela peut être utilisé pour obtenir une vignette de la première seconde de la vidéo
        }
    }

    override fun getItemCount() = mediaItems.size
}
