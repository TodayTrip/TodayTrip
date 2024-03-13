package com.twoday.todaytrip.ui.route

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemRouteListBinding

class RouteAdapter :
    ListAdapter<RouteListData, RouteAdapter.ViewHolder>(RouteListDataDiffCallback) {
        private val TAG = "RouteAdapter"

    var onRouteListDataClickListener: OnRouteListDataClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRouteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        Log.d(TAG, "onBindViewHolder) item.title: ${currentItem.name}, position: ${position}")
        holder.position.text = (position+1).toString()
        holder.initListener(currentItem)
        holder.bind(currentItem)
    }

    inner class ViewHolder(binding: ItemRouteListBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvSavePhotoRoadText
        val address = binding.tvSavePhotoAddress
        val option = binding.ivSavePhotoOption
        val position = binding.tvSavePhotoPocketNumber
        fun initListener(item: RouteListData) {
            itemView.setOnClickListener{
                Log.d(TAG, "RouteAdapter)setOnClickListener ${item.contentId}")
                onRouteListDataClickListener?.onRouteListDataClick(item.contentId)
            }
        }
        fun bind(item: RouteListData) {
            name.text = item.name
            address.text = item.address
        }

    }
}