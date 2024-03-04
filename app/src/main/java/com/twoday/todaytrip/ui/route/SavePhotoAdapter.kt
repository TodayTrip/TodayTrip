package com.twoday.todaytrip.ui.route

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ItemSavePhotoListBinding

class SavePhotoAdapter :
    ListAdapter<SavePhotoData, SavePhotoAdapter.itemViewHolder>(object :
        DiffUtil.ItemCallback<SavePhotoData>() {
        override fun areItemsTheSame(oldItem: SavePhotoData, newItem: SavePhotoData): Boolean {
            // 비디오 id가 같은지 확인
            return (oldItem.name == newItem.name) && (oldItem.num == newItem.num)
        }

        override fun areContentsTheSame(oldItem: SavePhotoData, newItem: SavePhotoData): Boolean {
            // 모든 필드가 같은지 확인 (data class의 equals 사용)
            return oldItem == newItem
        }
    }) {

    interface ItemClick {
        fun onClick(item:SavePhotoData)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): SavePhotoAdapter.itemViewHolder {
        val binding =
            ItemSavePhotoListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return itemViewHolder(binding)
    }

    override fun onBindViewHolder(holder: SavePhotoAdapter.itemViewHolder, position: Int) {
        holder.num.text = (position+1).toString()
        holder.bind(getItem(position))
        if (position == itemCount - 1) {
            holder.visi.visibility = View.INVISIBLE
        }
        holder.image.setOnClickListener {
            itemClick?.onClick(getItem(position))
        }
    }

    inner class itemViewHolder(binding: ItemSavePhotoListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val title = binding.tvSavePhotoRoadText
        val address = binding.tvSavePhotoAddress
        val num = binding.tvSavePhotoPocketNumber
        val visi = binding.viRouteLastView
        val icon = binding.ivSavePhotoIcon
        val icontext = binding.tvSavePhotoPlustext
        val image = binding.ivSavePhotoImage
        fun bind(item: SavePhotoData) {
            title.text = item.name
            address.text = item.address
            Glide.with(image).
            load(item.image).
            into(image)

        }
    }
}