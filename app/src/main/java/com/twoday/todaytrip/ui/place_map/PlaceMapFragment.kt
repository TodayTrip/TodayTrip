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
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
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
    private val viewModel by lazy {
        ViewModelProvider(this@PlaceMapFragment)[PlaceMapViewModel::class.java]
    }
    private val pagerSnapHelper = PagerSnapHelper()
    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    private val markers = mutableListOf<Marker>()

    private var locations =
        loadTouristAttractionList().map {
            LatLng(it.getLatitude().toDouble(), it.getLongitude().toDouble())
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
        _binding = FragmentPlaceMapBinding.inflate(inflater, container, false)
        Log.d(TAG,"onCreateView")
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        mapView = binding.mvPlaceMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initImageRecyclerView()
        initTabLayout()
        initModelObserver()
        Log.d(TAG,"onViewCreated")
    }

    private fun initOnChangeListener() {
        naverMap.addOnCameraChangeListener { _, _ ->
            val zoom = naverMap.cameraPosition.zoom
            if (zoom > 14.0) { // 확대 레벨이 임계값 이상일 때 마커 캡션 보이기
//                Log.d("마커사이즈",markers.size.toString())
                markers.forEach { marker ->
//                    Log.d("마커 tag",marker.tag.toString())
                    marker.captionText = marker.tag as? String ?: ""
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
        Log.d(TAG,"initOnChangeListener")
    }

    private fun initModelObserver() {
        viewModel.locations.observe(viewLifecycleOwner, Observer {
            locations = it
        })
        Log.d(TAG,"initModelObserver")
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
                        val pos = layoutManager?.getPosition(view!!)
                        pos?.let { moveToMarker(it)}
                    }
                }
            })
        }
        Log.d(TAG,"initImageRecyclerView")
    }

    private fun moveToMarker(position: Int) {
        val targetLocation = locations[position]
        val zoomLevel = 15.0 // 숫자가 커질 수록 확대됨
        val cameraPosition = CameraPosition(targetLocation, zoomLevel)
        val cameraUpdate = CameraUpdate.toCameraPosition(cameraPosition).animate(CameraAnimation.Easing)
        naverMap.moveCamera(cameraUpdate)
        Log.d(TAG,"moveToMarker")
    }

    private fun initTabLayout() {
        binding.tlTabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                viewModel.onTabSeleted(tab.position)
                when (tab.position) {
                    0 -> {
                        Log.d(TAG,"initTabLayout 1")
                        clearMarkers()
                        onMarkerReady()
                        placeMapAdapter.submitList(loadTouristAttractionList())
                    }
                    1 -> {
                        Log.d(TAG,"initTabLayout 2")
                        clearMarkers()
                        onMarkerReady()
                        placeMapAdapter.submitList(loadRestaurantList())
                    }
                    2 -> {
                        Log.d(TAG,"initTabLayout 3")
                        clearMarkers()
                        onMarkerReady()
                        placeMapAdapter.submitList(loadCafeList())
                    }
                    3 -> {
                        Log.d(TAG,"initTabLayout 4")
                        clearMarkers()
                        onMarkerReady()
                        placeMapAdapter.submitList(loadEventList())
                    }
                }
                onMarkerReady()
//                clearMarkers()
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
        Log.d(TAG,"onMapReady\n----------------------------------------------------------")
    }

    private fun onMarkerReady() {
//        clearMarkers()

        if (locations.isNotEmpty()) {
            val markerIconBitmap =
                resizeMapIcons(requireContext(), R.drawable.ic_white_circle_marker, 100, 100)

            locations.forEachIndexed { index, latLng ->
                val marker = Marker().apply {
                    position = latLng
                    icon = OverlayImage.fromBitmap(markerIconBitmap)
                    map = naverMap
                    if (index < placeMapAdapter.currentList.size) {
                        tag = placeMapAdapter.currentList[index].getTitle()
                    }
                }
                markers.add(marker) // 마커 리스트에 추가
            }

            val bounds = createBoundsForAllMarkers(markers)
            updateCameraToBounds(naverMap, bounds, 250)
        }

        initOnChangeListener()
        Log.d(TAG,"onMarkerReady")
    }

    //TODO ViewModel로 옮기려고 하는데 마커를 지도에서 제거하는 건 View에서 없애는 거니까 옮기면 안될까요?
    private fun clearMarkers() {
        markers.forEach { marker ->
            marker.map = null // 마커를 지도에서 제거
        }
        markers.clear() // 마커 리스트 비우기
        Log.d(TAG,"clearMarkers")
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
        super.onSaveInstanceState(outState)
        mapView.onSaveInstanceState(outState)
        Log.d(TAG,"onSaveInstanceState")
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        Log.d(TAG,"onDestroyView")
        _binding = null
    }
}