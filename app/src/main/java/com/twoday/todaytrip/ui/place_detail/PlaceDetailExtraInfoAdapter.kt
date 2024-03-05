package com.twoday.todaytrip.ui.place_detail

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemPlaceDetailExtraInfoBinding
import com.twoday.todaytrip.tourData.TourItem

class PlaceDetailExtraInfoAdapter: ListAdapter<TourItem, PlaceDetailExtraInfoAdapter.ViewHolder>(object :
    DiffUtil.ItemCallback<TourItem>() {
    override fun areItemsTheSame(oldItem: TourItem, newItem: TourItem): Boolean =
        oldItem.getContentId() == newItem.getContentId()

    @SuppressLint("DiffUtilEquals")
    override fun areContentsTheSame(oldItem: TourItem, newItem: TourItem): Boolean =
        oldItem == newItem
}) {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemPlaceDetailExtraInfoBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.onBind()
    }


    inner class ViewHolder(private val binding: ItemPlaceDetailExtraInfoBinding): RecyclerView.ViewHolder(binding.root) {
        fun onBind() {

        }
    }
}