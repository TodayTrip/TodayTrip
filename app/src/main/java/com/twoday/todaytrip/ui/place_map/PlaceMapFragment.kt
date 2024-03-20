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
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.PagerSnapHelper
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.tabs.TabLayout
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraAnimation
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Align
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentPlaceMapBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.utils.MapUtils.createBoundsForAllMarkers
import com.twoday.todaytrip.utils.MapUtils.resizeMapIcons
import com.twoday.todaytrip.utils.MapUtils.updateCameraToBounds
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadCafeList
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadEventList
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadRestaurantList
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadTouristAttractionList
import com.twoday.todaytrip.viewModel.PlaceMapViewModel

@RequiresApi(Build.VERSION_CODES.TIRAMISU)
class PlaceMapFragment : Fragment(), OnMapReadyCallback {
    private val TAG = "PlaceMapFragment"
    private var _binding: FragmentPlaceMapBinding? = null
    private val binding get() = _binding!!
    private val placeMapAdapter by lazy { PlaceMapAdapter(onItemClick) }
    private val viewModel: PlaceMapViewModel by viewModels()

    private val pagerSnapHelper = PagerSnapHelper()
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    private val markers = mutableListOf<Marker>()

    private var locations =
        loadTouristAttractionList().map {
            LocationInfo(
                LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble()),
                it.getTitle()
            )
        }

    private val onItemClick: (TourItem) -> Unit = { tourItem ->
        val placeDetailIntent = PlaceDetailActivity.newIntent(
            requireContext(),
            tourItem.getContentTypeId(),
            tourItem)
        startActivity(placeDetailIntent)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        Log.d(TAG,"onCreateView")
        _binding = FragmentPlaceMapBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        Log.d(TAG,"onViewCreated")

        mapView = binding.mvPlaceMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initImageRecyclerView()
        initTabLayout()
        initModelObserver()
    }

    private fun initOnChangeListener(position: Int) {
        Log.d(TAG,"initOnChangeListener")
        val locationInfo = locations[position]
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
        Log.d(TAG,"initModelObserver")
        viewModel.locations.observe(viewLifecycleOwner, Observer {
            locations = it
        })
    }

    private fun initImageRecyclerView() {
        Log.d(TAG,"initImageRecyclerView")
        pagerSnapHelper.attachToRecyclerView(binding.rvPlaceMap)
        binding.rvPlaceMap.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = placeMapAdapter
            addOnScrollListener(object : RecyclerView.OnScrollListener() {
                override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                    super.onScrollStateChanged(recyclerView, newState)
                    if (newState == RecyclerView.SCROLL_STATE_IDLE) {
                        val view = pagerSnapHelper.findSnapView(layoutManager)
                        val pos = layoutManager?.getPosition(view!!)
                        pos?.let { moveToMarker(it)}
                    }
                }
            })
        }
    }

    private fun moveToMarker(position: Int) {
        Log.d(TAG,"moveToMarker")
        val locationInfo = locations[position]
        val targetLocation = locationInfo.latLng
        val zoomLevel = 15.0 // 숫자가 커질 수록 확대됨
        val cameraPosition = CameraPosition(targetLocation, zoomLevel)
        val cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition).animate(CameraAnimation.Easing)

        naverMap.moveCamera(cameraUpdate)
        initOnChangeListener(position)
    }

    private fun initTabLayout() {
        Log.d(TAG,"initTabLayout")
        binding.tlTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.onTabSeleted(tab.position)
                clearMarkers()
                when (tab.position) {
                    0 -> {
                        Log.d(TAG,"initTabLayout 1")
                        placeMapAdapter.submitList(loadTouristAttractionList())
                    }
                    1 -> {
                        Log.d(TAG,"initTabLayout 2")
                        placeMapAdapter.submitList(loadRestaurantList())
                    }
                    2 -> {
                        Log.d(TAG,"initTabLayout 3")
                        placeMapAdapter.submitList(loadCafeList())
                    }
                    3 -> {
                        Log.d(TAG,"initTabLayout 4")
                        placeMapAdapter.submitList(loadEventList())
                    }
                }
                onMarkerReady()
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

        onMarkerReady()
    }

    private fun onMarkerReady() {
        Log.d(TAG,"onMarkerReady")

        if (locations.isNotEmpty()) {
            val markerIconBitmap =
                resizeMapIcons(requireContext(), R.drawable.ic_white_circle_marker, 100, 100)

            locations.forEachIndexed { index, locationInfo ->
                val marker = Marker().apply {
                    position = locationInfo.latLng
                    icon = OverlayImage.fromBitmap(markerIconBitmap)
                    map = naverMap
                    if (index < placeMapAdapter.currentList.size) {
                        tag = locationInfo.title
                    }
                }
                markers.add(marker) // 마커 리스트에 추가
            }

            val bounds = createBoundsForAllMarkers(markers)
            updateCameraToBounds(naverMap, bounds, 200)
        }
    }

    //TODO ViewModel로 옮기려고 하는데 마커를 지도에서 제거하는 건 View에서 없애는 거니까 옮기면 안될까요?
    private fun clearMarkers() {
        Log.d(TAG,"clearMarkers")
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
        super.onResume()
        mapView.onResume()
    }

    override fun onPause() {
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
        Log.d(TAG,"onSaveInstanceState")
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
    }

    override fun onDestroyView() {
        Log.d(TAG,"onDestroyView\n--------------------------------------")
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }
}

data class LocationInfo(
    val latLng: LatLng,
    val title: String
)