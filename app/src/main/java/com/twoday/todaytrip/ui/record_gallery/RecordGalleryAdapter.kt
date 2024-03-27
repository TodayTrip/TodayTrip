package com.twoday.todaytrip.ui.record_gallery

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ItemRecordGalleryPhotoBinding
import com.twoday.todaytrip.utils.glide

class RecordGalleryAdapter(private val uriList: List<String>) : RecyclerView.Adapter<RecordGalleryAdapter.ViewHolder>() {
    var itemClick: ItemClick? = null
    interface ItemClick {
        fun onClick(view: View, position: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecordGalleryPhotoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUri = uriList[position]
        holder.bind(imageUri)
        holder.itemView.setOnClickListener {
            if(itemClick != null) {
                itemClick!!.onClick(it,position)
            }
        }
    }

    override fun getItemCount(): Int = uriList.size

    inner class ViewHolder(private val binding: ItemRecordGalleryPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: String) {
            binding.galleryPhoto.glide(imageUri)
            Log.d("zxc",imageUri)

            binding.root.setOnClickListener {
                val position = adapterPosition
                if(position != RecyclerView.NO_POSITION){
                    itemClick?.onClick(it, position)
                }
            }
        }
    }
}
