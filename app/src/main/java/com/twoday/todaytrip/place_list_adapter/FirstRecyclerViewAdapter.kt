package com.twoday.todaytrip.place_list_adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ItemPlaceListBinding
import com.twoday.todaytrip.tourData.TourItem
class FirstRecyclerViewAdapter:
    ListAdapter<TourItem, FirstRecyclerViewAdapter.Holder>(TourItemDiffCallback) {
    private val TAG = "FirstRecyclerViewAdapter"

    val onTourItemClickListener:OnTourItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemPlaceListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }
    override fun onBindViewHolder(holder: Holder, position: Int) {
        holder.run{
            initOnClickListener(getItem(position))
            bind(getItem(position))
        }
    }

    inner class Holder(val binding: ItemPlaceListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val firstImageView = binding.ivItemPlaceList
        private val titleTextView = binding.tvItemPlaceListTitle
        private val addressTextView = binding.tvItemPlaceListAddress
        //TODO 영업시간, 휴무일 표시하기
        private val addButton = binding.btnItemPlaceListAdd

        fun bind(item: TourItem) {
            item.getThumbnailImage()?.let { url ->
                Glide.with(MyApplication.appContext!!)
                    .load(url)
                    .placeholder(R.drawable.img_default_image)
                    .into(firstImageView)
            }
            titleTextView.text = item.getTitle()
            addressTextView.text = item.getAddress()
        }

        fun initOnClickListener(tourItem:TourItem){
            this.addButton.setOnClickListener {
                onTourItemClickListener?.onTourItemClick(tourItem)
            }
        }
    }
}