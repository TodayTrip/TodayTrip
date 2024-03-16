package com.twoday.todaytrip.ui.place_detail

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityPlaceDetailBinding
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemAddClickListener

class PlaceDetailActivity : AppCompatActivity() {
    private val TAG = "PlaceDetailActivity"

    private lateinit var binding: ActivityPlaceDetailBinding

    private val model by lazy {
        ViewModelProvider(this@PlaceDetailActivity)[PlaceDetailViewModel::class.java]
    }

    private lateinit var placeInfoAdapter: PlaceDetailExtraInfoAdapter

    private val tourItemExtra by lazy {
        when (intent.getStringExtra(EXTRA_CONTENT_TYPE_ID)) {
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
                intent.getParcelableExtra<TourItem.TouristDestination>(EXTRA_TOUR_ITEM)
            }
        }
    }

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

        initViewModel()
        initTitleUI()
        initMoreInfoRecyclerView()
        initModelObserver()
        initBackButton()
        initAddButton()
    }

    private fun initViewModel() {
        model.initTourItem(tourItemExtra!!)
    }

    private fun initTitleUI() {
        tourItemExtra?.let {
            if (it.getImage().isNullOrEmpty()) {
                Glide.with(applicationContext.applicationContext)
                    .load(it.getImage())
                    .into(binding.ivPlaceDetailPic)
            }
            binding.tvPlaceDetailTitle.text = it.getTitle()
            binding.tvPlaceDetailAddress.text = it.getAddress()
        }
    }

    private fun initMoreInfoRecyclerView() {
        placeInfoAdapter = PlaceDetailExtraInfoAdapter().apply {
            placeInfoList = tourItemExtra!!.getDetailInfoWithLabel()
        }
        binding.rvPlaceDetailExtraInfoList.adapter = placeInfoAdapter
    }

    private fun initModelObserver() {
        model.isTourItemAdded.observe(this@PlaceDetailActivity) { isAdded ->
            binding.tvPlaceDetailAddPathBtn.background =
                this.resources.getDrawable(
                    if (isAdded) R.drawable.shape_main_blue_border_10_radius
                    else R.drawable.shape_main_blue_10_radius
                )
            binding.tvPlaceDetailAddPathBtn.text = this.resources.getText(
                if (isAdded) R.string.place_detail_remove_from_path
                else R.string.place_detail_add_to_path
            )
            binding.tvPlaceDetailAddPathBtn.setTextColor(
                this.resources.getColor(
                    if (isAdded) R.color.main_blue
                    else R.color.white
                )
            )
        }
    }

    private fun initBackButton(){
        binding.ivPlaceDetailBack.setOnClickListener {
            if (!isFinishing) finish()
        }
    }
    private fun initAddButton() {
        binding.tvPlaceDetailAddPathBtn.setOnClickListener {
            model.addButtonClicked()
        }
    }
}