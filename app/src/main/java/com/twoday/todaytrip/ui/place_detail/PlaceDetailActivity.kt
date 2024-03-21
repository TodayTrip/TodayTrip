package com.twoday.todaytrip.ui.place_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import androidx.transition.TransitionInflater
import com.bumptech.glide.Glide
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityPlaceDetailBinding
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemClickListener
import com.twoday.todaytrip.viewModel.PlaceDetailViewModel

class PlaceDetailActivity : AppCompatActivity() , OnTourItemClickListener{
    private val TAG = "PlaceDetailActivity"

    private lateinit var binding: ActivityPlaceDetailBinding

    private val model by lazy {
        ViewModelProvider(this@PlaceDetailActivity)[PlaceDetailViewModel::class.java]
    }

    private lateinit var placeInfoAdapter: PlaceInfoAdapter
    private lateinit var nearByAdapter: NearByAdapter
    private lateinit var memoryDataAdapter: MemoryDataAdapter

    private val tourItemExtra by lazy {
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
                Log.d(TAG, "tour item from intent extra is null!")
                null
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

        initTitleUI()
        initPlaceInfoRecyclerView()
        initNearByRecyclerView()
        initMyMemoryRecyclerView()

        initModelObserver()
        initViewModel()

        initBackButton()
        initAddButton()
    }

    private fun initViewModel() {
        tourItemExtra?.let {
            model.initTourItem(it)
        }
    }

    private fun initTitleUI() {
        tourItemExtra?.let {
            Log.d(TAG, "tourItem is not null")

            Log.d(TAG, "image: ${it.getImage()}, thumbnail: ${it.getThumbnailImage()}")
            if (!it.getImage().isNullOrEmpty()) {
                Glide.with(this@PlaceDetailActivity)
                    .load(it.getImage().toString())
                    .into(binding.ivPlaceDetailPic)
            } else if (!it.getThumbnailImage().isNullOrEmpty()) {
                Glide.with(this@PlaceDetailActivity)
                    .load(it.getThumbnailImage().toString())
                    .into(binding.ivPlaceDetailPic)
            }
            binding.tvPlaceDetailTitle.text = it.getTitle()
            binding.tvPlaceDetailAddress.text = it.getAddress()
        }
    }

    private fun initPlaceInfoRecyclerView() {
        placeInfoAdapter = PlaceInfoAdapter(listOf())
        binding.rvPlaceDetailExtraInfoList.adapter = placeInfoAdapter
    }

    private fun initNearByRecyclerView(){
        nearByAdapter = NearByAdapter().apply{
            onTourItemClickListener = this@PlaceDetailActivity
        }
        binding.rvPlaceDetailNearby.adapter = nearByAdapter
    }

    private fun initMyMemoryRecyclerView() {
        memoryDataAdapter = MemoryDataAdapter()
        binding.rvPlaceDetailMyMemory.adapter = memoryDataAdapter
    }

    override fun onTourItemClick(tourItem: TourItem) {
        Log.d(TAG, "onTourItemClick) current TourItem: ${tourItemExtra!!.getTitle()} -> clicked TourItem: ${tourItem.getTitle()}")
        startActivity(
            newIntent(this@PlaceDetailActivity, tourItem.getContentTypeId(), tourItem)
        )
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

        model.placeInfoList.observe(this@PlaceDetailActivity) {
            placeInfoAdapter.setDataSet(it)
        }
        model.nearByList.observe(this@PlaceDetailActivity){
            Log.d(TAG, "observe) nearByList.size: ${it.size}")
            nearByAdapter.changeDataSet(it)
        }
        model.isLoadingNearByList.observe(this@PlaceDetailActivity){isLoading ->
            setLoadingUI(isLoading)
            if(!isLoading && model.nearByList.value.isNullOrEmpty())
                binding.tvPlaceDetailNoNearby.isVisible = true
        }

        model.memoryDataList.observe(this@PlaceDetailActivity) {
            Log.d(TAG, "observe) memoryDataList.size: ${it.size}")
            memoryDataAdapter.submitList(it.toMutableList())

            binding.rvPlaceDetailMyMemory.isVisible = it.isNotEmpty()
            binding.tvPlaceDetailNoMemory.isVisible = it.isEmpty()
        }
    }

    private fun setLoadingUI(isLoading:Boolean){
        binding.shimmerPlaceDetailNearby.run{
            startShimmer()
            isVisible = isLoading
        }
        binding.rvPlaceDetailNearby.isVisible = !isLoading
    }

    private fun initBackButton() {
        binding.ivPlaceDetailBack.setOnClickListener {
            if (!isFinishing) finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }
    }

    private fun initAddButton() {
        binding.tvPlaceDetailAddPathBtn.setOnClickListener {
            model.addButtonClicked()
        }
    }
}