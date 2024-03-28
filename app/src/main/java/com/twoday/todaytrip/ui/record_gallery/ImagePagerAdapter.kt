package com.twoday.todaytrip.ui.record_gallery

import android.net.Uri
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemFullScreenImageBinding
import com.twoday.todaytrip.utils.glide

class ImagePagerAdapter(private val imageUris: List<String>) :
    RecyclerView.Adapter<ImagePagerAdapter.ImageViewHolder>() {

    inner class ImageViewHolder(private val binding: ItemFullScreenImageBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(uri: Uri) {
            binding.imageView.glide(uri)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemFullScreenImageBinding.inflate(inflater, parent, false)
        return ImageViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val uri = Uri.parse(imageUris[position])
        holder.bind(uri)
    }

    override fun getItemCount(): Int = imageUris.size
}
