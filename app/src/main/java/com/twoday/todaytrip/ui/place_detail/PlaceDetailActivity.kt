package com.twoday.todaytrip.ui.place_detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.activity.viewModels
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityPlaceDetailBinding
import com.twoday.todaytrip.place_list_adapter.OnTourItemAddClickListener
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class PlaceDetailActivity : AppCompatActivity() {
    private val TAG = "PlaceDetailActivity"

    private lateinit var binding: ActivityPlaceDetailBinding

    //private val viewModel: PlaceDetailViewModel by viewModels()

    //대표사진 한장만
//    private val placePhotoAdapter by lazy { PlaceDetailPhotoAdapter() }
    private val placeInfoAdapter by lazy { PlaceDetailExtraInfoAdapter() }
    private val placeMemoryAdapter by lazy { PlaceDetailMyMemoryAdapter() }

    //getExtra(장소 TourItem parcelable)
    private val tourItem:TourItem? by lazy{
        when(intent.getStringExtra(EXTRA_CONTENT_TYPE_ID)){
            TourContentTypeId.TOURIST_DESTINATION.contentTypeId ->{
                intent.getParcelableExtra<TourItem.TouristDestination>(EXTRA_TOUR_ITEM)
            }
            TourContentTypeId.CULTURAL_FACILITIES.contentTypeId ->{
                intent.getParcelableExtra<TourItem.CulturalFacilities>(EXTRA_TOUR_ITEM)
            }
            TourContentTypeId.RESTAURANT.contentTypeId ->{
                intent.getParcelableExtra<TourItem.Restaurant>(EXTRA_TOUR_ITEM)
            }
            TourContentTypeId.LEISURE_SPORTS.contentTypeId ->{
                intent.getParcelableExtra<TourItem.LeisureSports>(EXTRA_TOUR_ITEM)
            }
            TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId ->{
                intent.getParcelableExtra<TourItem.EventPerformanceFestival>(EXTRA_TOUR_ITEM)
            }
            else -> {
                null
            }
        }
    }
    companion object{
        const val EXTRA_CONTENT_TYPE_ID = "extra_content_type_id"
        const val EXTRA_TOUR_ITEM = "extra_tour_item"
        fun newIntent(context: Context, contentTypeId:String, tourItem:TourItem): Intent =
            Intent(context, PlaceDetailActivity::class.java).apply {
                putExtra(EXTRA_CONTENT_TYPE_ID, contentTypeId)
                putExtra(EXTRA_TOUR_ITEM, tourItem)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d(TAG, "tourItem title = ${tourItem?.getTitle()}")

        initView()
        //initViewModel()
    }

    private fun initView() {
//        val introPhotoRecyclerView = binding.rvPlaceDetailPic
        val extraInfoRecyclerView = binding.rvPlaceDetailExtraInfoList
        val myMemoryRecyclerView = binding.rvPlaceDetailMyMemoryList
//        introPhotoRecyclerView.adapter = placePhotoAdapter
        extraInfoRecyclerView.adapter = placeInfoAdapter
        myMemoryRecyclerView.adapter = placeMemoryAdapter
    }


//    @SuppressLint("SetTextI18n")
//    private fun initViewModel() =viewModel.also { vm ->
//        vm.placeItemData.observe(this, Observer {
//            it.getContentId()
//            it.getDetailInfoWithLabel()
//            with(binding) {
//                Glide.with(MyApplication.appContext!!)
//                    .load(it.getThumbnailImage())
//                    .placeholder(R.drawable.img_shop_default)
//                    .into(binding.ivPlaceDetailPic)
//                tvPlaceDetailTitle.text = it.getTitle()
//                tvPlaceDetailLoca.text = "주소  ${it.getAddress()}"
//                tvPlaceDetailTime.text = "영업시간  ${it.getDetailInfoWithLabel()}"
//                tvPlaceDetailInfo.text
//            }
//        })
//        vm.placeMemory.observe(this, Observer {
//        })
//    }

    fun initOnClickListener(item: TourItem) {
        binding.tvPlaceDetailAddPathBtn.setOnClickListener {
            OnTourItemAddClickListener.onTourItemAddClick(item)
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