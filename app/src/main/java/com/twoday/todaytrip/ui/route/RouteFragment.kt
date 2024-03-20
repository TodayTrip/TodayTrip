package com.twoday.todaytrip.ui.route

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.recyclerview.widget.ItemTouchHelper
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.util.FusedLocationSource
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRouteBinding
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.ui.save_photo.SavePhotoActivity
import com.twoday.todaytrip.utils.MapUtils
import com.twoday.todaytrip.utils.MapUtils.drawPolyline
import com.twoday.todaytrip.utils.TourItemPrefUtil
import com.twoday.todaytrip.tourData.TourItem
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.transition.TransitionInflater
import com.naver.maps.map.overlay.PolylineOverlay
import com.twoday.todaytrip.ui.save_photo.SavePhotoAdapter
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.viewModel.RouteViewModel

class RouteFragment : Fragment(), OnMapReadyCallback, OnRouteListDataClickListener,
    OnMoveEndListener {
    private val TAG = "RouteFragment"

    private lateinit var binding: FragmentRouteBinding

    private val routeAdapter: RouteAdapter by lazy {
        RouteAdapter()
    }

    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource
    private val polylineOverlay = PolylineOverlay()

    private val markers = mutableListOf<Marker>()
    private val routeViewModel by viewModels<RouteViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // mapView 변수 초기화
        mapView = binding.mvRoute
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initModelObserver()
        initRouteRecyclerView()
        initItemTouchSimpleCallback()
        initRouteButton()
        initToolTip()
    }

    private fun initModelObserver() {
        routeViewModel.routeListDataSet.observe(viewLifecycleOwner) { routeDataList ->
            routeViewModel.getLocation()
            routeAdapter.submitList(routeDataList.toMutableList())
            binding.layoutRouteEmptyFrame.isVisible = routeDataList.isEmpty()

            if (routeViewModel.isMapReady.value!!) {
                routeViewModel.getLocation()
            }
        }

        routeViewModel.editMode.observe(viewLifecycleOwner) { editMode ->
            routeAdapter.iconTogle(editMode)
            binding.tvRouteRemoveButton.isVisible = !editMode
            binding.tvRouteComplitButton.isVisible = editMode
        }

        routeViewModel.isMapReady.observe(viewLifecycleOwner) { isMapReady ->
            if (isMapReady) routeViewModel.getLocation()
        }

        routeViewModel.locations.observe(viewLifecycleOwner) { locations ->
            if (!routeViewModel.isMapReady.value!!) return@observe
            clearMarkers()
            polylineOverlay.map = null
            if (locations.isNotEmpty()) {
                val markerIconBitmap =
                    MapUtils.resizeMapIcons(
                        MyApplication.appContext!!,
                        R.drawable.img_pic_marker,
                        100,
                        100
                    )
                locations.forEach { latLng ->
                    val marker = Marker().apply {
                        position = latLng
                        icon = OverlayImage.fromBitmap(markerIconBitmap)
                        map = naverMap
                    }
                    markers.add(marker) // 마커 리스트에 추가
                }
                val bounds = MapUtils.createBoundsForAllMarkers(markers)

                MapUtils.updateCameraToBounds(naverMap, bounds, 130)

                if (locations.size == 1) {
                    naverMap.moveCamera(CameraUpdate.zoomTo(13.0))
                }
            } else {
                // TODO : locations에 저장된 값이 없을 때 현재 선택한 지역이 카메라에 보이게 수정
            }

            if (locations.size > 1) {
                val markerPositions = locations.map { location ->
                    LatLng(location.latitude, location.longitude)
                }
                polylineOverlay.map = null
                polylineOverlay.apply {
                    coords = markerPositions
                    color = 0xFF0085FF.toInt()
                    map = naverMap
                }
            }
        }
    }

    private fun initRouteRecyclerView() {
        routeAdapter.onRouteListDataClickListener = this@RouteFragment
        binding.rvRouteRecyclerview.adapter = routeAdapter
    }

    //디테일 화면 넘기기
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onRouteListDataClick(contentId: String) {
        Log.d(TAG, "fragment)onRouteListDataClick) ${contentId}")
        val allTourItemList = TourItemPrefUtil.loadAllTourItemList()
        val clickedTourItem: TourItem = allTourItemList.find {
            it.getContentId() == contentId
        }!!
        startActivity(
            PlaceDetailActivity.newIntent(
                MyApplication.appContext!!,
                clickedTourItem.getContentTypeId(),
                clickedTourItem
            )
        )
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    //drag & drop
    private fun initItemTouchSimpleCallback() {
        val itemTouchHelper = ItemTouchHelper(
            ItemTouchSimpleCallback().apply {
                onMoveEndListener = this@RouteFragment
            }
        )
        itemTouchHelper.attachToRecyclerView(
            binding.rvRouteRecyclerview
        )
    }

    override fun onMoveEnd() {
        Log.d(TAG, "onMoveEnd) called")
        routeViewModel.getLocation()
    }

    //클릭 이벤트
    private fun initRouteButton() {
        binding.layoutRouteFinishButton.setOnClickListener {
            if (routeViewModel.routeListDataSet.value?.isNotEmpty() == true) {
                activity?.let {
                    val intent = Intent(context, SavePhotoActivity::class.java)
                    startActivity(intent)
                    requireActivity().overridePendingTransition(R.anim.slide_in, R.anim.fade_out)
                }
            } else Toast.makeText(context, "경로를 추가해 주세요", Toast.LENGTH_SHORT).show()
        }

        binding.tvRouteRemoveButton.setOnClickListener {
            routeViewModel.toggleEditMode()
        }
        binding.tvRouteComplitButton.setOnClickListener {
            routeViewModel.toggleEditMode()
        }
    }

    override fun onRouteListDataRemove(item: RouteListData, position: Int) {
        routeViewModel.dataRemove(item, position)
        routeViewModel.getLocation()
    }

    private fun initToolTip() {
        //tooltip 버튼
        binding.ivTooltip.setOnClickListener {
            val balloon = context?.let { it1 ->
                createBalloon(it1) {
                    setWidthRatio(1.0f)
                    setHeight(BalloonSizeSpec.WRAP)
                    setText("기록이 저장되며 해당탭은 초기화 됩니다\n저장한 기록은 기록 탭에서 보실 수 있습니다")
                    setTextColorResource(R.color.black)
                    setTextSize(15f)
                    setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                    setArrowSize(10)
                    setArrowPosition(0.5f)
                    setPadding(12)
                    setMarginLeft(20)
                    setMarginRight(20)
                    setCornerRadius(8f)
                    elevation
                    setBackgroundColorResource(R.color.white)
                    setBalloonAnimation(BalloonAnimation.ELASTIC)
                    setLifecycleOwner(lifecycleOwner)
                    build()
                }
            }
            balloon?.showAlignBottom(binding.ivTooltip)
            Handler(Looper.getMainLooper()).postDelayed({
                balloon?.dismiss()
            }, 3000)
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap
        routeViewModel.setIsMapReady(true)
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
        routeViewModel.getRouteDataSet()
        super.onResume()
    }

    override fun onPause() {
        routeViewModel.setIsMapReady(false)
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