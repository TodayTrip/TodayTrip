package com.twoday.todaytrip.ui.record_gallery

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ItemRecordGalleryPhotoBinding
import com.twoday.todaytrip.databinding.ItemRecordGalleryTagBinding
import javax.microedition.khronos.opengles.GL

class RecordGalleryAdapter(private val imageUriList: List<String>) : RecyclerView.Adapter<RecordGalleryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecordGalleryPhotoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUri = imageUriList[position]
        holder.bind(imageUri)
    }

    override fun getItemCount(): Int = imageUriList.size

    class ViewHolder(private val binding: ItemRecordGalleryPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: String) {
            Glide.with(binding.galleryPhoto.context)
                .load(imageUri)
                .into(binding.galleryPhoto)
        }
    }
}
