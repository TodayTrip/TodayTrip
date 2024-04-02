package com.twoday.todaytrip.ui.route

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.view.isVisible
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

        holder.bind(currentItem, position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int, payloads: MutableList<Any>) {
        if (payloads.isEmpty())
            super.onBindViewHolder(holder, position, payloads)
        else
            holder.bindPositionTextView(position)
    }

    fun iconTogle(show: Boolean) {
        if (editMode != show) {
            editMode = show
            notifyDataSetChanged() // 모든 아이템을 다시 바인딩하도록 함
        }
        Log.d("sdc", "adapter $editMode")
//        submitList()
    }

    inner class ViewHolder(binding: ItemRouteListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val name = binding.tvSavePhotoRoadText
        private val address = binding.tvSavePhotoAddress
        private val option = binding.ivSavePhotoOption
        private val positionTextView: TextView = binding.tvSavePhotoPocketNumber
        private val view: ImageView = binding.ivSavePhotoPocketColor
        private val icon = binding.ivRouteRemove

        fun bindPositionTextView(position: Int){
            positionTextView.text = (position + 1).toString()
        }

        fun bind(item: RouteListData, position: Int) {
            name.text = item.name
            address.text = item.address
            positionTextView.text = (position + 1).toString()

            icon.isVisible = editMode
            view.isVisible = !editMode
            positionTextView.isVisible = !editMode

            initListener(item, position)
        }

        private fun initListener(item: RouteListData, position: Int) {
            itemView.setOnClickListener {
                Log.d(TAG, "RouteAdapter)setOnClickListener ${item.contentId}")
                onRouteListDataClickListener?.onRouteListDataClick(item.contentId)
            }

            icon.setOnClickListener {
                onRouteListDataClickListener?.onRouteListDataRemove(item, position)
                Toast.makeText(it.context, "경로가 삭제되었습니다.", Toast.LENGTH_SHORT).show()
            }
        }

        fun iconTogleEdit(show: Boolean) {
//            getItem(0).remove=true
            if (editMode != show) {
                editMode = !show
            }
//        submitList()
        }

    }
}