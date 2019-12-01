package com.example.imaginecup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.imaginecup.room.Photo
import kotlinx.android.synthetic.main.view_photo.view.*

class PhotosAdapter(
    private val context: Context,
    private val itemClickListener: (Photo) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var showingItems: MutableList<Photo> = mutableListOf()
    private var items: List<Photo> = listOf()

    override fun getItemCount() = items.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.view_photo, parent, false)
        return PhotoViewHolder(layout, itemClickListener)
    }

    override fun getItemViewType(position: Int) = PHOTO_VIEW_TYPE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val photo = items[position]
        val photoViewHolder = viewHolder as PhotoViewHolder
        photoViewHolder.photo = photo
        photoViewHolder.ivPhoto.setImageBitmap(photo.bitmap)
    }

    fun setItems(photosList: List<Photo>) {
        items = photosList
        showingItems = photosList.toMutableList()
        notifyDataSetChanged()
    }

    class PhotoViewHolder(view: View, itemClickListener: (Photo) -> Unit) :
        RecyclerView.ViewHolder(view) {
        lateinit var photo: Photo
        val ivPhoto = view.ivPhoto

        init {
            view.setOnClickListener {
                itemClickListener.invoke(photo)
            }
        }
    }

    companion object {
        const val PHOTO_VIEW_TYPE = 0
    }
}