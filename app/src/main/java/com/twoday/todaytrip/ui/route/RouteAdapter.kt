package com.twoday.todaytrip.ui.route

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.databinding.ItemRouteListBinding

class RouteAdapter :
    ListAdapter<RouteListData, RouteAdapter.ViewHolder>(RouteListDataDiffCallback) {
        private val TAG = "RouteAdapter"

    var onRouteListDataClickListener: OnRouteListDataClickListener? = null
    private var editMode: Boolean = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding =
            ItemRouteListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val currentItem = getItem(position)
        Log.d(TAG, "onBindViewHolder) item.title: ${currentItem.name}, position: ${position}")
        holder.position.text = (position+1).toString()
        holder.icon.visibility = if (editMode) View.VISIBLE else View.GONE
        holder.view.visibility = if (editMode) View.INVISIBLE else View.VISIBLE
        holder.position.visibility = if (editMode) View.INVISIBLE else View.VISIBLE
        holder.initListener(currentItem)
        holder.bind(currentItem)

        holder.icon.setOnClickListener {
            onRouteListDataClickListener?.onRouteListDataRemove(currentItem, position)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if(payloads.isEmpty()) super.onBindViewHolder(holder, position, payloads)
        else{
            holder.position.text = (position+1).toString()
        }
    }

    fun iconTogle(show: Boolean){
        if (editMode != show) {
            editMode = show
            notifyDataSetChanged() // 모든 아이템을 다시 바인딩하도록 함
        }
        Log.d("sdc","adapter $editMode")
//        submitList()
    }

    inner class ViewHolder(binding: ItemRouteListBinding) : RecyclerView.ViewHolder(binding.root) {
        val name = binding.tvSavePhotoRoadText
        val address = binding.tvSavePhotoAddress
        val option = binding.ivSavePhotoOption
        val position = binding.tvSavePhotoPocketNumber
        val view = binding.viSavePhotoPocketColor
        val icon = binding.ivRouteRemove

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

        fun iconTogleEdit(show: Boolean){
//            getItem(0).remove=true
            if (editMode != show) {
                editMode = !show
            }

//        submitList()
        }

    }
}