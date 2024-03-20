package com.twoday.todaytrip.ui.save_photo

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ItemSavePhotoListBinding

class SavePhotoAdapter :
    ListAdapter<SavePhotoData, SavePhotoAdapter.ItemViewHolder>(
        object : DiffUtil.ItemCallback<SavePhotoData>() {
            //        추가부분
            override fun areItemsTheSame(oldItem: SavePhotoData, newItem: SavePhotoData): Boolean {
                // 비디오 id가 같은지 확인
                return oldItem.imageUriList == newItem.imageUriList
            }

            override fun areContentsTheSame(
                oldItem: SavePhotoData,
                newItem: SavePhotoData
            ): Boolean {
                // 모든 필드가 같은지 확인 (data class의 equals 사용)
                return oldItem == newItem
            }
        }) {
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
        holder.bind(getItem(position))
        if (position == itemCount - 1) {
            holder.visi.visibility = View.INVISIBLE
        }
        holder.image.setOnClickListener {
            itemClick?.onClick(getItem(position), position)
        }
    }

    fun addImagesUriList(uriList: MutableList<String>, position: Int) {
        getItem(position).imageUriList = uriList
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

            if (!item.imageUriList.isNullOrEmpty()) {
                Glide.with(image)
                    .load(item.imageUriList[0])
                    .into(image)
                icon.isVisible = false
                iconText.isVisible = false
                image.clipToOutline = true
            }
        }
    }
}