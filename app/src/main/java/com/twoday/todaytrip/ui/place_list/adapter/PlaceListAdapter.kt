package com.twoday.todaytrip.ui.place_list.adapter

import android.graphics.ColorMatrix
import android.graphics.ColorMatrixColorFilter
import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ItemPlaceListBinding
import com.twoday.todaytrip.databinding.ItemPlaceListSkeletonShimmerBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.tourData.removeDestination
import com.twoday.todaytrip.pref_utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.glide

enum class PlaceListViewType(val viewType: Int) {
    TOUR_ITEM(0),
    DUMMY_ITEM(1)
}

class PlaceListAdapter :
    ListAdapter<TourItem, RecyclerView.ViewHolder>(TourItemDiffCallback) {
    private val TAG = "PlaceListAdapter"

    private val DUMMY_COUNT = 3
    var onTourItemClickListener: OnTourItemClickListener? = null

    override fun getItemViewType(position: Int): Int =
        if (getItem(position) != PlaceListConstants.DUMMY_TOUR_ITEM) {
            PlaceListViewType.TOUR_ITEM.viewType
        } else {
            PlaceListViewType.DUMMY_ITEM.viewType
        }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder =
        when (viewType) {
            PlaceListViewType.TOUR_ITEM.viewType -> {
                val binding =
                    ItemPlaceListBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                TourItemHolder(binding)
            }

            else -> {
                val binding =
                    ItemPlaceListSkeletonShimmerBinding.inflate(
                        LayoutInflater.from(parent.context), parent, false
                    )
                DummyTourItemHolder(binding)
            }
        }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is TourItemHolder) {
            holder.run {
                initOnClickListener(getItem(position))
                bind(getItem(position))
            }
        } else {
            (holder as DummyTourItemHolder).bind()
        }
    }

    override fun submitList(list: MutableList<TourItem>?) {
        Log.d(TAG, "submitList) submitted list size: ${list?.size}")

        if (list != null) {
            list.forEach {
                it.isAdded = ContentIdPrefUtil.isSavedContentId(it.getContentId())
            }

            val newList = mutableListOf<TourItem>()
            newList.addAll(list.filter { it.isAdded })
            newList.addAll(list.filter { !it.isAdded })

            list.clear()
            list.addAll(newList)
        }
        super.submitList(list)
    }

    fun addDummyTourItem() {
        val newListWithDummy = mutableListOf<TourItem>()
        newListWithDummy.run {
            addAll(currentList)
            for (i in 0 until DUMMY_COUNT)
                add(PlaceListConstants.DUMMY_TOUR_ITEM)
        }
        submitList(newListWithDummy)
    }

    inner class TourItemHolder(val binding: ItemPlaceListBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val firstImageView = binding.ivItemPlaceListThumbnail
        private val titleTextView = binding.tvItemPlaceListTitle
        private val addressTextView = binding.tvItemPlaceListAddress

        private val timeTextViewList = listOf(
            binding.tvItemPlaceListTime1,
            binding.tvItemPlaceListTime2
        )

        fun bind(item: TourItem) {
            firstImageView.glide(item.getImage())
            firstImageView.clipToOutline = true

            if (item.isAdded) {
                binding.ivBookmark.visibility = View.VISIBLE
            } else {
                binding.ivBookmark.visibility = View.INVISIBLE
            }

            when (item) {
                is TourItem.EventPerformanceFestival -> {
                    if (item.isEventPerformanceFestivalOver()) {
                        val filter = ColorMatrixColorFilter(
                            ColorMatrix().apply {
                                setSaturation(0F)
                            }
                        )
                        firstImageView.colorFilter = filter
                        binding.layoutEntire.setBackgroundResource(R.color.light_gray)
                    } else {
                        firstImageView.colorFilter = null
                        binding.layoutEntire.setBackgroundResource(R.color.white)
                    }
                }

                else -> {
                    // do nothing
                }
            }

            titleTextView.text = item.getTitle()
            addressTextView.text = item.getAddress().removeDestination()

            item.getTimeInfoWithLabel().forEachIndexed{ index, pair ->
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

    inner class DummyTourItemHolder(val binding: ItemPlaceListSkeletonShimmerBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind() {
            Log.d(TAG, "bind dummy TourItem for loading UI")
            binding.shimmerItemPlaceListSkeletonShimmer.startShimmer()
        }
    }
}