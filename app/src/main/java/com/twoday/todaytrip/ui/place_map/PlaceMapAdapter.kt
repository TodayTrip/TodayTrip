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
import com.twoday.todaytrip.place_list_adapter.OnTourItemAddClickListener
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
//        holder.run {
////            initOnClickListener(getItem(position))
//            bind(getItem(position))
//        }
        val item = getItem(position) // ListAdapter에서는 getItem() 메서드 사용
        holder.bind(item)
    }

    inner class Holder(val binding: ItemPlaceMapListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val firstImageView = binding.ivItemPlaceList
        private val titleTextView = binding.tvItemPlaceListTitle
        private val addressTextView = binding.tvItemPlaceListAddress

        //TODO 영업시간, 휴무일 표시하기
        private val addButton = binding.btnItemPlaceListMapAdd

        fun bind(item: TourItem) {
            binding.tvItemPlaceListTitle.text = item.getTitle()
            binding.tvItemPlaceListAddress.text = item.getAddress()
            Glide.with(MyApplication.appContext!!)
                .load(item.getThumbnailImage())
                .placeholder(R.drawable.img_default_image)
                .into(binding.ivItemPlaceList)
            binding.ivItemPlaceList.clipToOutline = true
        }
        fun initOnClickListener(item: TourItem) {
            this.addButton.setOnClickListener {
                onTourItemClickListener?.onTourItemAddClick(item)
                setAddButtonUI(item.isAdded)
            }
        }
        private fun setAddButtonUI(isAdded: Boolean) {
            addButton.background = MyApplication.appContext!!.resources.getDrawable(
                if (isAdded) R.drawable.shape_white_with_border_radius_10
                else R.drawable.shape_mainblue_10_radius
            )
            addButton.text = MyApplication.appContext!!.resources.getText(
                if (isAdded) R.string.item_place_list_remove
                else R.string.item_place_list_add
            )
            addButton.setTextColor(
                MyApplication.appContext!!.resources.getColor(
                    if (isAdded) R.color.main_blue
                    else R.color.white
                )
            )
        }
    }
}