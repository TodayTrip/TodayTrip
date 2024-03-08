package com.twoday.todaytrip.ui.place_map

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ItemPlaceMapListBinding
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemAddClickListener
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.TourItemPrefUtil

class PlaceMapAdapter() : ListAdapter<TourItem, PlaceMapAdapter.Holder>(
    TourItemDiffCallback()
) {
    init {
        submitList(TourItemPrefUtil.loadTouristAttractionList())
    }
    var onTourItemClickListener: OnTourItemAddClickListener? = null
    class TourItemDiffCallback : DiffUtil.ItemCallback<TourItem>() {
        override fun areItemsTheSame(oldItem: TourItem, newItem: TourItem): Boolean {
            return oldItem.getContentId() == newItem.getContentId()
        }
        override fun areContentsTheSame(oldItem: TourItem, newItem: TourItem): Boolean {
            return oldItem.getAddress() == newItem.getAddress()
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaceMapAdapter.Holder {
        val binding =
            ItemPlaceMapListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.run {
            bind(getItem(position))
        }
    }

    inner class Holder(val binding: ItemPlaceMapListBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: TourItem) {
            binding.tvItemPlaceMapTitle.text = item.getTitle()
            binding.tvItemPlaceMapAddress.text = item.getAddress()

            Glide.with(MyApplication.appContext!!)
                .load(item.getThumbnailImage())
                .placeholder(R.drawable.img_default)
                .into(binding.ivItemPlaceMapThumbnail)
            binding.ivItemPlaceMapThumbnail.clipToOutline = true

            val timeInfo = item.getTimeInfoWithLabel()
            binding.tvItemPlaceListTime1.text = timeInfo[0].second
            binding.tvItemPlaceListTime2.text = timeInfo[1].second
        }
    }
}