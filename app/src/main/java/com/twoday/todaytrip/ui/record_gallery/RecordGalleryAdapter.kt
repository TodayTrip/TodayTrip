package com.twoday.todaytrip.ui.record_gallery

import android.content.Intent
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ItemRecordGalleryPhotoBinding
import com.twoday.todaytrip.databinding.ItemRecordGalleryTagBinding
import com.twoday.todaytrip.ui.place_list.FullScreenImageActivity
import javax.microedition.khronos.opengles.GL

class RecordGalleryAdapter(private val uriList: List<String>) : RecyclerView.Adapter<RecordGalleryAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemRecordGalleryPhotoBinding.inflate(inflater, parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val imageUri = uriList[position]
        holder.bind(imageUri)
    }

    override fun getItemCount(): Int = uriList.size

    class ViewHolder(private val binding: ItemRecordGalleryPhotoBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(imageUri: String) {
            Glide.with(binding.galleryPhoto.context)
                .load(imageUri)
                .into(binding.galleryPhoto)
            Log.d("zxc",imageUri)

            binding.galleryPhoto.setOnClickListener {
                val context = binding.root.context
                val intent = Intent(context, FullScreenImageActivity::class.java).apply {
                    putExtra("imageUri", imageUri)

                }
                context.startActivity(intent)
            }
        }
    }
}
