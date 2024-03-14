package com.twoday.todaytrip.ui.place_list.adapter

import android.text.Html
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
import com.twoday.todaytrip.utils.ContentIdPrefUtil

class PlaceListAdapter :
    ListAdapter<TourItem, PlaceListAdapter.Holder>(TourItemDiffCallback) {
    private val TAG = "PlaceListRecyclerViewAdapter"

    var onTourItemClickListener: OnTourItemClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding =
            ItemPlaceListBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        Log.d(TAG, "onBindViewHolder) item title : ${getItem(position).getTitle()}")
        holder.run {
            initOnClickListener(getItem(position))
            bind(getItem(position))
        }
    }

    override fun submitList(list: MutableList<TourItem>?) {
        Log.d(TAG, "submitList) submitted list size: ${currentList.size}")
        list?.forEach {
            it.isAdded = ContentIdPrefUtil.isSavedContentId(it.getContentId())
        }
        super.submitList(list)
    }

    inner class Holder(val binding: ItemPlaceListBinding) : RecyclerView.ViewHolder(binding.root) {
        private val firstImageView = binding.ivItemPlaceListThumbnail
        private val titleTextView = binding.tvItemPlaceListTitle
        private val addressTextView = binding.tvItemPlaceListAddress

        private val timeTextViewList = listOf(
            binding.tvItemPlaceListTime1,
            binding.tvItemPlaceListTime2
        )

        fun bind(item: TourItem) {
            item.getThumbnailImage()?.let { url ->
                Glide.with(MyApplication.appContext!!)
                    .load(url)
                    .placeholder(R.drawable.img_default)
                    .into(firstImageView)
            }
            firstImageView.clipToOutline = true

            titleTextView.text = item.getTitle()
            addressTextView.text = item.getAddress()

            item.getTimeInfoWithLabel().forEachIndexed { index, pair ->
                Log.d(TAG, "getTimeInfoWithLabel) index: $index, pair: $pair")
                timeTextViewList[index].text = Html.fromHtml(pair.second)
            }
        }

        fun initOnClickListener(item: TourItem) {
            this.itemView.setOnClickListener {
                Log.d(TAG, "itemView.setOnClickListener) called")
                onTourItemClickListener?.onTourItemClick(item)
            }
        }
    }
}