package com.twoday.todaytrip.ui.place_detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityPlaceDetailBinding
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemAddClickListener
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.ContentIdPrefUtil

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class PlaceDetailActivity : AppCompatActivity() {
    private val TAG = "PlaceDetailActivity"

    private lateinit var binding: ActivityPlaceDetailBinding

    //private val viewModel: PlaceDetailViewModel by viewModels()

    //    private val placePhotoAdapter by lazy { PlaceDetailPhotoAdapter() }
    private lateinit var placeInfoAdapter: PlaceDetailExtraInfoAdapter
//    private val placeMemoryAdapter by lazy { PlaceDetailMyMemoryAdapter() }

    //getExtra(장소 TourItem parcelable)
    private val tourItem: TourItem? by lazy {
        when (intent.getStringExtra(EXTRA_CONTENT_TYPE_ID)) {
            TourContentTypeId.TOURIST_DESTINATION.contentTypeId -> {
                intent.getParcelableExtra<TourItem.TouristDestination>(EXTRA_TOUR_ITEM)
            }

            TourContentTypeId.CULTURAL_FACILITIES.contentTypeId -> {
                intent.getParcelableExtra<TourItem.CulturalFacilities>(EXTRA_TOUR_ITEM)
            }

            TourContentTypeId.RESTAURANT.contentTypeId -> {
                intent.getParcelableExtra<TourItem.Restaurant>(EXTRA_TOUR_ITEM)
            }

            TourContentTypeId.LEISURE_SPORTS.contentTypeId -> {
                intent.getParcelableExtra<TourItem.LeisureSports>(EXTRA_TOUR_ITEM)
            }

            TourContentTypeId.EVENT_PERFORMANCE_FESTIVAL.contentTypeId -> {
                intent.getParcelableExtra<TourItem.EventPerformanceFestival>(EXTRA_TOUR_ITEM)
            }

            else -> {
                null
            }
        }
    }

    private val hasPhoto: Boolean = false

    companion object {
        const val EXTRA_CONTENT_TYPE_ID = "extra_content_type_id"
        const val EXTRA_TOUR_ITEM = "extra_tour_item"
        fun newIntent(context: Context, contentTypeId: String, tourItem: TourItem): Intent =
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
        tourItem!!.isAdded = ContentIdPrefUtil.isSavedContentId(tourItem!!.getContentId())

        initView()
        //initViewModel()
        initOnClickListener(tourItem!!)
    }

    private fun initView() {
//        val introPhotoRecyclerView = binding.rvPlaceDetailPic
        val extraInfoRecyclerView = binding.rvPlaceDetailExtraInfoList
//        val myMemoryRecyclerView = binding.rvPlaceDetailMyMemoryList
//        introPhotoRecyclerView.adapter = placePhotoAdapter

        placeInfoAdapter = PlaceDetailExtraInfoAdapter(tourItem!!.getDetailInfoWithLabel())
        extraInfoRecyclerView.adapter = placeInfoAdapter
        Log.d("PlaceDetailInfo", "itemCount = ${placeInfoAdapter.itemCount}")
//        myMemoryRecyclerView.adapter = placeMemoryAdapter

        tourItem?.getDetailInfoWithLabel()

        with(binding) {
            tvPlaceDetailTitle.text = tourItem?.getTitle()
            tvPlaceDetailLoca.text = tourItem?.getAddress()
//            tvPlaceDetailTime.text = tourItem?.getTimeInfoWithLabel()?.get(0)?.second ?: "00:00"
            Glide.with(applicationContext.applicationContext)
                .load(tourItem?.getImage())
                .into(ivPlaceDetailPic)
            tvPlaceDetailNoPhoto.isVisible = !hasPhoto
        }
        Log.d("btn", "${tourItem!!.isAdded}")
        setAddButtonUI(tourItem!!.isAdded)
    }


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

    private fun initOnClickListener(item: TourItem) {
        binding.ivPlaceDetailBack.setOnClickListener {
            if (!isFinishing) finish()
        }
        binding.tvPlaceDetailAddPathBtn.setOnClickListener {
            OnTourItemAddClickListener.onTourItemAddClick(item)
            setAddButtonUI(item.isAdded)
        }
    }

    @SuppressLint("UseCompatLoadingForDrawables")
    private fun setAddButtonUI(isAdded: Boolean) {
        binding.tvPlaceDetailAddPathBtn.background =
            MyApplication.appContext!!.resources.getDrawable(
                if (isAdded) R.drawable.shape_main_blue_border_10_radius
                else R.drawable.shape_main_blue_10_radius
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