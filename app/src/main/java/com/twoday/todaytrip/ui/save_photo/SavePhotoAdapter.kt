package com.twoday.todaytrip.ui.save_photo

import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ItemSavePhotoListBinding

class SavePhotoAdapter(private val item: MutableList<SavePhotoData>) :
    RecyclerView.Adapter<SavePhotoAdapter.ItemViewHolder>() {
    interface ItemClick {
        fun onClick(item: SavePhotoData, position: Int)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemViewHolder {
        val binding =
            ItemSavePhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ItemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        holder.num.text = (position + 1).toString()
        holder.bind(item[position])
        if (position == itemCount - 1) {
            holder.visi.visibility = View.INVISIBLE
        }
        holder.image.setOnClickListener {
            itemClick?.onClick(item[position], position)
        }
    }

    override fun getItemCount(): Int {
        return item.size
    }

    fun addImageUri(uri: Uri, position: Int) {
        item[position].imageUri = uri.toString()
        notifyItemChanged(position)
    }

    inner class ItemViewHolder(binding: ItemSavePhotoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvSavePhotoRoadText
        val address = binding.tvSavePhotoAddress
        val num = binding.tvSavePhotoPocketNumber
        val visi = binding.viRouteLastView
        val icon = binding.ivSavePhotoIcon
        val iconText = binding.tvSavePhotoPlustext
        val image = binding.ivSavePhotoImage

        fun bind(item: SavePhotoData) {
            title.text = item.tourItem.getTitle()
            address.text = item.tourItem.getAddress()

            if(!item.imageUri.isNullOrBlank()) {
                icon.isVisible = false
                iconText.isVisible = false

                Glide.with(image)
                    .load(item.imageUri)
                    .into(image)
            }
        }
    }
}