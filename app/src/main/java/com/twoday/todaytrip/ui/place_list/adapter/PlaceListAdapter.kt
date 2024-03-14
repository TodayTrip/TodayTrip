package com.twoday.todaytrip.ui.place_list.adapter

import android.annotation.SuppressLint
import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
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
import com.twoday.todaytrip.tourApi.IntroDetailItem
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.tourData.removeDestination
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.DateTimeUtil

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
        holder.run {
            initOnClickListener(getItem(position))
            bind(getItem(position))
        }
    }

    override fun submitList(list: MutableList<TourItem>?) {
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
            val currentDate = DateTimeUtil.getCurrentDateWithNoLine()
            val currentTime = DateTimeUtil.getCurrentTime()
            val currentDay = DateTimeUtil.getCurrentDay()

            item.getThumbnailImage()?.let { url ->
                Glide.with(MyApplication.appContext!!)
                    .load(url)
                    .placeholder(R.drawable.img_default)
                    .into(firstImageView)
            }

            when (item.getContentTypeId()) {
                "15" -> {//행사축제
                    val startDate = item.getDetailInfoWithLabel()[3].second
                    val endDate = item.getDetailInfoWithLabel()[4].second
                    if (currentDate > endDate) {
                        val matrix = ColorMatrix()
                        matrix.setSaturation(0F)
                        val filter = ColorMatrixColorFilter(matrix)
                        firstImageView.setColorFilter(filter)
                        binding.layoutEntire.setBackgroundResource(R.color.light_gray)
                    }
                }
                "39" -> {//식당카페
                }
                else -> {//관관지
                }
            }

            Log.d("date", "current date=${DateTimeUtil.getCurrentDateWithNoLine()} place date=${item.getTimeInfoWithLabel()[1].second} detail info=${item.getDetailInfoWithLabel()}")
            firstImageView.clipToOutline = true

            titleTextView.text = item.getTitle()
            addressTextView.text = item.getAddress().removeDestination()

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