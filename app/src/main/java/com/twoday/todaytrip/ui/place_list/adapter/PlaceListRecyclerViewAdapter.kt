package com.twoday.todaytrip.ui.place_list.adapter

import android.text.Html
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ItemPlaceListBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.ContentIdPrefUtil

class PlaceListRecyclerViewAdapter :
    ListAdapter<TourItem, PlaceListRecyclerViewAdapter.Holder>(TourItemDiffCallback) {
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
        private val firstImageView = binding.ivItemPlaceMapThumbnail
        private val titleTextView = binding.tvItemPlaceListTitle
        private val addressTextView = binding.tvItemPlaceListAddress

        private val timeLabelTextViewList = listOf(
            binding.tvItemPlaceListTimeLabel1,
            binding.tvItemPlaceListTimeLabel2,
            binding.tvItemPlaceListTimeLabel3
        )
        private val timeTextViewList = listOf(
            binding.tvItemPlaceListTime1,
            binding.tvItemPlaceListTime2,
            binding.tvItemPlaceListTime3
        )

        private val addLayout = binding.layoutItemPlaceListAdd
        private val addTextView = binding.tvItemPlaceListAdd

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
                with(timeLabelTextViewList[index]) {
                    isVisible = true
//                    text = pair.first
                }
                with(timeTextViewList[index]) {
                    isVisible = true
                    text = Html.fromHtml(pair.second)
                }
            }

            setAddButtonUI(item.isAdded)
        }

        fun initOnClickListener(item: TourItem) {
            this.itemView.setOnClickListener {
                Log.d(TAG, "itemView.setOnClickListener) called")
                onTourItemClickListener?.onTourItemClick(item)
            }

            this.addLayout.setOnClickListener {
                OnTourItemAddClickListener.onTourItemAddClick(item)
                setAddButtonUI(item.isAdded)
            }
        }

        private fun setAddButtonUI(isAdded: Boolean) {
            addLayout.background = MyApplication.appContext!!.resources.getDrawable(
                if (isAdded) R.drawable.shape_main_blue_border_10_radius
                else R.drawable.shape_main_blue_10_radius
            )
            addTextView.text = MyApplication.appContext!!.resources.getText(
                if (isAdded) R.string.item_place_list_remove
                else R.string.item_place_list_add
            )
            addTextView.setTextColor(
                MyApplication.appContext!!.resources.getColor(
                    if (isAdded) R.color.main_blue
                    else R.color.white
                )
            )
        }
    }
}