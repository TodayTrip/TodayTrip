package com.twoday.todaytrip.ui.route

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ItemSavePhotoListBinding

class SavePhotoAdapter(private val item: MutableList<SavePhotoData>) :
    RecyclerView.Adapter<SavePhotoAdapter.itemViewHolder>(){


    interface ItemClick {
        fun onClick(item:SavePhotoData,position: Int)
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
        holder.bind(item[position])
        if (position == itemCount - 1) {
            holder.visi.visibility = View.INVISIBLE
        }
        holder.image.setOnClickListener {
            itemClick?.onClick(item[position],position)
        }
    }

    override fun getItemCount(): Int {
        return item.size
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