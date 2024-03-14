package com.twoday.todaytrip.ui.record

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.ViewHolder
import com.bumptech.glide.Glide
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ItemRecordBinding

class RecordAdapter : ListAdapter<Record, RecordAdapter.Holder>(RecordDiffCallback) {
    private val TAG = "RecordAdapter"

    var onRecordClickListener:OnRecordClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): Holder {
        val binding = ItemRecordBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return Holder(binding)
    }

    override fun onBindViewHolder(holder: Holder, position: Int) {
        with(getItem(position)){
            holder.initListener(this)
            holder.bind(this)
        }
    }

    override fun submitList(list: MutableList<Record>?) {
        super.submitList(list?.sortedBy { it.travelDate }?.reversed())
    }

    inner class Holder(binding: ItemRecordBinding) : ViewHolder(binding.root) {
        private val imageView: ImageView = binding.ivItemRecordImage
        private val titleTextView: TextView = binding.tvItemRecordTitle
        private val dateTextView: TextView = binding.tvItemRecordDate

        fun initListener(record: Record){
            itemView.setOnClickListener {
                onRecordClickListener?.onRecordClick(record)
            }
        }
        fun bind(record: Record) {
            val savePhotoDataWithImage = record.savePhotoDataList.find {
//                !it.imageUri.isNullOrBlank()
                !it.imageUriList.isNullOrEmpty()
            }
            if (savePhotoDataWithImage != null) {
                Glide.with(MyApplication.appContext!!)
                    .load(savePhotoDataWithImage.imageUriList[0])
                    .placeholder(R.drawable.ic_launcher)
                    .into(imageView)
            }
            else{
                imageView.setImageResource(R.drawable.ic_launcher)
            }
            imageView.clipToOutline = true

            titleTextView.text = record.destination
            dateTextView.text = record.travelDate
        }
    }
}