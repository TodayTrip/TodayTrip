package com.twoday.todaytrip.place_list_adapter

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

class TouristAttractionRecyclerViewAdapter :
    ListAdapter<TourItem, TouristAttractionRecyclerViewAdapter.Holder>(TourItemDiffCallback) {
    private val TAG = "FirstRecyclerViewAdapter"

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
            firstImageView.clipToOutline = true
            titleTextView.text = item.getTitle()
            addressTextView.text = item.getAddress()

            setAddButtonUI(item.isAdded)
        }

        fun initOnClickListener(item: TourItem) {
            this.addButton.setOnClickListener {
                OnTourItemClickListener.onTourItemClick(item)
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