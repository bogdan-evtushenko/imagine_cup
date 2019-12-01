package com.example.imaginecup

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.recyclerview.widget.RecyclerView
import com.example.imaginecup.room.Photo
import com.google.gson.Gson
import edmt.dev.edmtdevcognitivevision.Contract.AnalysisResult
import kotlinx.android.synthetic.main.view_photo.view.*

class PhotosAdapter(
    private val context: Context,
    private val itemClickListener: (Photo) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    private var showingItems: MutableList<Photo> = mutableListOf()
    private var items: List<Photo> = listOf()

    override fun getItemCount() = showingItems.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val layout = LayoutInflater.from(context).inflate(R.layout.view_photo, parent, false)
        return PhotoViewHolder(layout, itemClickListener)
    }

    override fun getItemViewType(position: Int) = PHOTO_VIEW_TYPE

    override fun onBindViewHolder(viewHolder: RecyclerView.ViewHolder, position: Int) {
        val photo = showingItems[position]
        val photoViewHolder = viewHolder as PhotoViewHolder
        photoViewHolder.photo = photo
        photoViewHolder.ivPhoto.setImageBitmap(photo.bitmap)
    }

    fun setItems(photosList: List<Photo>) {
        items = photosList
        showingItems.clear()
        showingItems.addAll(photosList)
        notifyDataSetChanged()
    }

    private fun checkFilterPatter(filterPattern: String, analysisResult: AnalysisResult): Boolean {
        if (filterPattern.isEmpty()) return false
        analysisResult.description.captions.forEach {
            if (it.text.contains(filterPattern)) {
                return true
            }
        }
        analysisResult.description.tags.forEach {
            if (it.contains(filterPattern)) {
                return true
            }
        }
        analysisResult.tags.forEach {
            if (it.name.contains(filterPattern)) {
                return true
            }
        }
        analysisResult.categories.forEach {
            if (it.name.contains(filterPattern)) {
                return true
            }
        }
        analysisResult.faces.forEach {
            if (it.age.toString().contains(filterPattern)) {
                return true
            }
        }
        return false
    }

    private val photoFilter: Filter = object : Filter() {
        override fun performFiltering(constraint: CharSequence?): FilterResults {
            val filteredList: MutableList<Photo> = ArrayList()
            if (constraint == null || constraint.isEmpty()) {
                filteredList.addAll(items)
            } else {
                val filterPatterns = constraint.split("&").map { it.trim() }
                println("filter patterns : $filterPatterns")
                for (item in items) {
                    val analysisResult = Gson().fromJson(item.jsonData, AnalysisResult::class.java)
                    var isContatinsSmth = false
                    for (filterPattern in filterPatterns) {
                        if (checkFilterPatter(filterPattern, analysisResult)) {
                            isContatinsSmth = true
                            break
                        }
                    }
                    if (isContatinsSmth) {
                        filteredList.add(item)
                    }
                }
            }
            val results = FilterResults()
            results.values = filteredList
            return results
        }

        override fun publishResults(
            constraint: CharSequence,
            results: FilterResults
        ) {
            println("${results.values}")
            showingItems.clear()
            showingItems.addAll(results.values as List<Photo>)
            notifyDataSetChanged()
        }
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

    override fun getFilter() = photoFilter
}