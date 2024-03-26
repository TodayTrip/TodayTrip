package com.twoday.todaytrip.ui.place_detail

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.bumptech.glide.Glide
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityPlaceDetailBinding
import com.twoday.todaytrip.tourData.TourContentTypeId
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemClickListener
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.MapUtils
import com.twoday.todaytrip.utils.MapUtils.resizeMapIcons
import com.twoday.todaytrip.viewModel.PlaceDetailViewModel

class PlaceDetailActivity : AppCompatActivity(), OnTourItemClickListener, OnMapReadyCallback {
    private val TAG = "PlaceDetailActivity"

    private lateinit var binding: ActivityPlaceDetailBinding

    private val model by lazy {
        ViewModelProvider(this@PlaceDetailActivity)[PlaceDetailViewModel::class.java]
    }

    private lateinit var placeInfoAdapter: PlaceInfoAdapter
    private lateinit var nearByAdapter: NearByAdapter
    private lateinit var memoryDataAdapter: MemoryDataAdapter

    private val mapView by lazy {
        binding.mapPlaceDetailNearby
    }
    private lateinit var naverMap: NaverMap
    private val markers = mutableListOf<Marker>()

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

        mapView.onCreate(savedInstanceState)

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
            binding.tvPlaceDetailScrollTitle.text = it.getTitle()
            binding.tvPlaceDetailAddress.text = it.getAddress()
        }
    }

    private fun initPlaceInfoRecyclerView() {
        placeInfoAdapter = PlaceInfoAdapter(listOf())
        binding.rvPlaceDetailExtraInfoList.adapter = placeInfoAdapter
    }

    private fun initNearByRecyclerView() {
        nearByAdapter = NearByAdapter().apply {
            onTourItemClickListener = this@PlaceDetailActivity
        }
        binding.rvPlaceDetailNearby.adapter = nearByAdapter
    }

    private fun initMyMemoryRecyclerView() {
        memoryDataAdapter = MemoryDataAdapter()
        binding.rvPlaceDetailMyMemory.adapter = memoryDataAdapter
    }

    override fun onTourItemClick(tourItem: TourItem) {
        Log.d(
            TAG,
            "onTourItemClick) current TourItem: ${tourItemExtra!!.getTitle()} -> clicked TourItem: ${tourItem.getTitle()}"
        )
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
                if (isAdded) {
                    R.string.place_detail_remove_from_path
                }
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
        model.nearByList.observe(this@PlaceDetailActivity) { locations ->
            nearByAdapter.changeDataSet(locations)
            if (model.isMapReady.value!!) {
                model.nearByList.value?.let { drawNearByListMarker(it) }
            }
        }
        model.isLoadingNearByList.observe(this@PlaceDetailActivity) { isLoading ->
            setLoadingUI(isLoading)
            if (!isLoading && model.nearByList.value.isNullOrEmpty())
                binding.tvPlaceDetailNoNearby.isVisible = true
        }
        model.memoryDataList.observe(this@PlaceDetailActivity) {
            Log.d(TAG, "observe) memoryDataList.size: ${it.size}")
            memoryDataAdapter.submitList(it.toMutableList())

            binding.rvPlaceDetailMyMemory.isVisible = it.isNotEmpty()
            binding.tvPlaceDetailNoMemory.isVisible = it.isEmpty()
        }
        model.isMapReady.observe(this@PlaceDetailActivity) { isMapReady ->
            if (isMapReady) {
                model.nearByList.value?.let { drawNearByListMarker(it) }
            }
        }
    }

    private fun drawNearByListMarker(locations: List<TourItem>) {
        var currentTitle: String? = null
        var currentLatLng: LatLng? = null
        tourItemExtra?.let {
            currentTitle = it.getTitle()
            currentLatLng = LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())

            val markerIconBitmap =
                resizeMapIcons(this, R.drawable.ic_white_circle_marker, 100, 100)

            val currentMarker = Marker().apply {
                position = currentLatLng!!
                captionText = currentTitle as String
                icon = OverlayImage.fromBitmap(markerIconBitmap)
                map = naverMap
            }
            markers.add(currentMarker)
        }
        if (locations.isNotEmpty()) {
            locations.forEachIndexed { index, location ->
                val latLng =
                    LatLng(location.getLatitude().toDouble(), location.getLongitude().toDouble())

                if (currentTitle != location.getTitle() || !markers.any { it.captionText == currentTitle }) {
                    val markerIconBitmap =
                        resizeMapIcons(this, R.drawable.ic_white_circle_marker, 70, 70)

                    val marker = Marker().apply {
                        position = latLng
                        captionText = location.getTitle()
                        icon = OverlayImage.fromBitmap(markerIconBitmap)
                        map = naverMap
                    }
                    markers.add(marker) // 마커 리스트에 추가
                }
            }
            val bounds = MapUtils.createBoundsForAllMarkers(markers)

            MapUtils.updateCameraToBounds(naverMap, bounds, 150)

            if (locations.size == 1) {
                naverMap.moveCamera(CameraUpdate.zoomTo(13.0))
            }
        } else {
            val destination = DestinationPrefUtil.loadDestination()
            val location = DestinationData.destinationLatLng[destination]
            val cameraUpdate = location?.let { CameraUpdate.scrollTo(it) }
            if (cameraUpdate != null) {
                naverMap.moveCamera(cameraUpdate)
            }

            naverMap.moveCamera(CameraUpdate.zoomTo(8.0))
        }
    }


    private fun setLoadingUI(isLoading: Boolean) {
        binding.shimmerPlaceDetailNearby.run {
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
            if(binding.tvPlaceDetailAddPathBtn.text == "경로에 추가하기") Toast.makeText(this@PlaceDetailActivity, "경로 탭에서 삭제되었습니다.",
                Toast.LENGTH_SHORT).show()
            else Toast.makeText(this@PlaceDetailActivity, "경로 탭에 추가되었습니다.", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        naverMap.uiSettings.isZoomControlEnabled = false
        model.setIsMapReady(true)
    }

    private fun clearMarkers() {
        markers.forEach { marker ->
            marker.map = null // 마커를 지도에서 제거
        }
        markers.clear() // 마커 리스트 비우기
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        mapView.onResume()
        mapView.getMapAsync(this)
        super.onResume()
    }

    override fun onPause() {
        model.setIsMapReady(false)
        mapView.onPause()
        super.onPause()
    }

    override fun onStop() {
        mapView.onStop()
        super.onStop()
    }

    override fun onLowMemory() {
        super.onLowMemory()
        mapView.onLowMemory()
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }
}