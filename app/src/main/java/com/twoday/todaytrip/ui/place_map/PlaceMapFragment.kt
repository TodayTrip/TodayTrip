package com.twoday.todaytrip.ui.place_map

import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.naver.maps.geometry.LatLng
import com.naver.maps.geometry.LatLngBounds
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.twoday.todaytrip.MapModel
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentPlaceMapBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.pref_utils.TourItemPrefUtil.loadCafeList
import com.twoday.todaytrip.pref_utils.TourItemPrefUtil.loadEventList
import com.twoday.todaytrip.pref_utils.TourItemPrefUtil.loadRestaurantList
import com.twoday.todaytrip.pref_utils.TourItemPrefUtil.loadTouristAttractionList
import com.twoday.todaytrip.tourData.TourItemJsonConverter
import com.twoday.todaytrip.viewModel.PlaceMapViewModel
import ted.gun0912.clustering.naver.TedNaverClustering

data class LocationInfo(
    val latLng: LatLng,
    val title: String
)

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class PlaceMapFragment : Fragment(), OnMapReadyCallback {
    private val TAG = "PlaceMapFragment"

    private var _binding: FragmentPlaceMapBinding? = null
    private val binding get() = _binding!!

    private val model: PlaceMapViewModel by viewModels()

    private val onItemClick: (TourItem) -> Unit = { tourItem ->
        val placeDetailIntent = PlaceDetailActivity.newIntent(
            requireContext(), TourItemJsonConverter.toJson(tourItem)
        )

        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        startActivity(placeDetailIntent)
    }
    private val placeMapAdapter =  PlaceMapAdapter(onItemClick)
    private val pagerSnapHelper = PagerSnapHelper()

    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    private val markers = mutableListOf<Marker>()
    private var tedNaverClustering: TedNaverClustering<MapModel>? = null

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG, "onCreateView")
        _binding = FragmentPlaceMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG, "onViewCreated")

        mapView = binding.mvPlaceMap
        mapView.onCreate(savedInstanceState)

        initImageRecyclerView()
        initTabLayout()
        initModelObserver()
    }

    private fun initOnChangeListener(position: Int) {
        val locationInfo = model.locations.value!![position]
        val targetTitle = locationInfo.title
        naverMap.addOnCameraChangeListener { _, _ ->
            val zoom = naverMap.cameraPosition.zoom
            if (zoom > 14.0) { // 확대 레벨이 임계값 이상일 때 마커 캡션 보이기
                markers.forEach { marker ->
                    marker.captionText = targetTitle
                    marker.setCaptionAligns(Align.Bottom)
                    marker.captionColor = Color.BLACK
                    marker.captionHaloColor = Color.WHITE
                    marker.captionTextSize = 16f
                }
            } else {
                // 확대 레벨이 임계값 미만일 때 마커 캡션 숨기기
                markers.forEach { marker ->
                    marker.captionText = ""
                }
            }
        }
    }

    private fun initModelObserver() {
        model.locations.observe(viewLifecycleOwner){locations ->
            if (model.isMapReady.value!!) drawMarkersOnMap(locations)
        }
        model.isMapReady.observe(viewLifecycleOwner){isMapReady ->
            if(isMapReady) drawMarkersOnMap(model.locations.value!!)
        }
    }

    private fun initImageRecyclerView() {
        pagerSnapHelper.attachToRecyclerView(binding.rvPlaceMap)

        binding.rvPlaceMap.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = placeMapAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val view = pagerSnapHelper.findSnapView(layoutManager)
                        val pos = view?.let{view ->
                            layoutManager?.getPosition(view)
                        }
                        pos?.let { moveToMarker(it) }
                    }
                }
            })
        }
    }

    private fun moveToMarker(position: Int) {
        Log.d(TAG, "moveToMarker")
        val locationInfo = model.locations.value!![position]
        val targetLocation = locationInfo.latLng
        val zoomLevel = 17.0 // 숫자가 커질 수록 확대됨
        val cameraPosition = CameraPosition(targetLocation, zoomLevel)
        val cameraUpdate =
            CameraUpdate.toCameraPosition(cameraPosition).animate(CameraAnimation.Easing)

        naverMap.moveCamera(cameraUpdate)
    }

    private fun initTabLayout() {
        Log.d(TAG, "initTabLayout")
        binding.tlTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                model.onTabSeleted(tab.position)
                when (tab.position) {
                    0 -> placeMapAdapter.submitList(loadTouristAttractionList())
                    1 -> placeMapAdapter.submitList(loadRestaurantList())
                    2 -> placeMapAdapter.submitList(loadCafeList())
                    3 -> placeMapAdapter.submitList(loadEventList())
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                // 탭이 선택 해제될 때 호출
            }

            override fun onTabReselected(tab: TabLayout.Tab) {
                // 이미 선택된 탭이 다시 선택될 때 호출
            }
        })
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        model.setIsMapReady(true)
    }

    private fun drawMarkersOnMap(locations:List<LocationInfo>) {
        clearMarkers()
        clearClusteringItems()

        val naverItems = mutableListOf<MapModel>()
        if (locations.isNotEmpty()) {
            val boundsBuilder = LatLngBounds.Builder()
            locations.forEachIndexed { index, locationInfo ->
                naverItems.add(MapModel(locationInfo.latLng, index))
                boundsBuilder.include(locationInfo.latLng)
            }
            val bounds = boundsBuilder.build()
            val cameraUpdate = CameraUpdate.fitBounds(bounds, 200)
            naverMap.moveCamera(cameraUpdate)
        }

        tedNaverClustering = TedNaverClustering.with<MapModel>(requireContext(), naverMap)
            .items(naverItems)
            .customMarker { naverItem ->
                Marker().apply {
                    icon = OverlayImage.fromResource(R.drawable.ic_white_circle_marker)
                    width = 100
                    height = 100
                    position = LatLng(
                        naverItem.getTedLatLng().latitude,
                        naverItem.getTedLatLng().longitude
                    )
                }
            }
            .clusterBuckets(intArrayOf(300, 625, 1250, 2500, 5000)) // default: intArrayOf(10, 20, 50, 100, 200, 500, 1000)
            .markerClickListener { clusterItem ->
                clusterItem.index?.let { scrollToRecyclerViewPosition(it) }
            }.make()
    }

    private fun scrollToRecyclerViewPosition(position: Int) {
        binding.rvPlaceMap.layoutManager?.let { layoutManager ->
            (layoutManager as LinearLayoutManager).scrollToPositionWithOffset(
                position + 1,
                binding.rvPlaceMap.width
            )
        }
    }

    private fun clearMarkers() {
        markers.forEach { marker ->
            marker.map = null
        }
        markers.clear()
    }
    private fun clearClusteringItems(){
        tedNaverClustering?.clearItems()
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        super.onResume()
        mapView.getMapAsync(this)
        mapView.onResume()
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
        Log.d(TAG, "onSaveInstanceState")
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        Log.d(TAG, "onDestroyView\n--------------------------------------")
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }
}