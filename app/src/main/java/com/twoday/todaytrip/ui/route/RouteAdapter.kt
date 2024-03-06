package com.twoday.todaytrip.ui.route

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemRouteListBinding

class RouteAdapter :
    ListAdapter<RouteListData, RouteAdapter.ViewHolder>(RouteListDataDiffCallback) {

    interface ItemClick {
        fun onClick(item: RouteListData)
    }

    var itemClick: ItemClick? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRouteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)
    }

    inner class ViewHolder(binding: ItemRouteListBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvSavePhotoRoadText
        val address = binding.tvSavePhotoAddress
        val option = binding.ivSavePhotoOption
        fun bind(item: RouteListData) {
            name.text = item.name
            address.text = item.address
        }
    }
}