package com.twoday.todaytrip.ui.place_detail

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityPlaceDetailBinding
import com.twoday.todaytrip.place_list_adapter.OnTourItemClickListener
import com.twoday.todaytrip.tourData.TourItem

class PlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceDetailBinding

    private val viewModel: PlaceDetailViewModel by viewModels()

    //대표사진 한장만
//    private val placePhotoAdapter by lazy { PlaceDetailPhotoAdapter() }
    private val placeInfoAdapter by lazy { PlaceDetailExtraInfoAdapter() }
    private val placeMemoryAdapter by lazy { PlaceDetailMyMemoryAdapter() }

    //getExtra(장소 TourItem parcelable)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
        initViewModel()
    }

    private fun initView() {
//        val introPhotoRecyclerView = binding.rvPlaceDetailPic
        val extraInfoRecyclerView = binding.rvPlaceDetailExtraInfoList
        val myMemoryRecyclerView = binding.rvPlaceDetailMyMemoryList
//        introPhotoRecyclerView.adapter = placePhotoAdapter
        extraInfoRecyclerView.adapter = placeInfoAdapter
        myMemoryRecyclerView.adapter = placeMemoryAdapter



    }

    @SuppressLint("SetTextI18n")
    private fun initViewModel() =viewModel.also { vm ->
        vm.placeItemData.observe(this, Observer {
            it.getContentId()
            it.getDetailInfoWithLabel()
            with(binding) {
                Glide.with(MyApplication.appContext!!)
                    .load(it.getThumbnailImage())
                    .placeholder(R.drawable.img_shop_default)
                    .into(binding.ivPlaceDetailPic)
                tvPlaceDetailTitle.text = it.getTitle()
                tvPlaceDetailLoca.text = "주소  ${it.getAddress()}"
                tvPlaceDetailTime.text = "영업시간  ${it.getDetailInfoWithLabel()}"
                tvPlaceDetailInfo.text
            }
        })
        vm.placeMemory.observe(this, Observer {
        })


    }

    fun initOnClickListener(item: TourItem) {
        binding.tvPlaceDetailAddPathBtn.setOnClickListener {
            OnTourItemClickListener.onTourItemClick(item)
            setAddButtonUI(item.isAdded)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setAddButtonUI(isAdded: Boolean) {
        binding.tvPlaceDetailAddPathBtn.background =
            MyApplication.appContext!!.resources.getDrawable(
                if (isAdded) R.drawable.shape_white_with_border_radius_10
                else R.drawable.shape_mainblue_10_radius
            )
        binding.tvPlaceDetailAddPathBtn.text = MyApplication.appContext!!.resources.getText(
            if (isAdded) R.string.place_detail_remove_from_path
            else R.string.place_detail_add_to_path
        )
        binding.tvPlaceDetailAddPathBtn.setTextColor(
            MyApplication.appContext!!.resources.getColor(
                if (isAdded) R.color.main_blue
                else R.color.white
            )
        )
    }


}