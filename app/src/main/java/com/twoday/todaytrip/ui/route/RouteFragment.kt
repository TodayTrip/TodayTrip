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
import androidx.annotation.RequiresApi
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
import com.twoday.todaytrip.viewModel.RouteViewModel

//class RouteFragment : Fragment(), OnMapReadyCallback, OnRouteListDataClickListener {
//    private val TAG = "RouteFragment"
//
//    private lateinit var binding: FragmentRouteBinding
//
//    private lateinit var dataSet: List<RouteListData>
//    private val adapter: RouteAdapter by lazy {
//        RouteAdapter()
//    }
//
//    private lateinit var naverMap: NaverMap
//    private lateinit var mapView: MapView
//    private lateinit var locationSource: FusedLocationSource
//
//    private val markers = mutableListOf<Marker>()
//    private var locations: MutableList<LatLng> = mutableListOf()
//    private val routeViewModel by viewModels<RouteViewModel>()
//
////    private val viewModel by lazy {
////        ViewModelProvider(this@RouteFragment)[RouteViewModel::class.java]
////    }
//
//
//    override fun onCreateView(
//        inflater: LayoutInflater, container: ViewGroup?,
//        savedInstanceState: Bundle?
//    ): View? {
//        binding = FragmentRouteBinding.inflate(inflater, container, false)
//        return binding.root
//    }
//
//    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
//        super.onViewCreated(view, savedInstanceState)
//
//        // mapView 변수 초기화
//        mapView = binding.mvRoute
//        mapView.onCreate(savedInstanceState)
//        mapView.getMapAsync(this)
//
//        initDataSet()
//        initRouteRecyclerView()
//        initItemTouchSimpleCallback()
//        initRouteFinishButton()
//        initToolTip()
//    }
//
//    private fun initDataSet() {
//        val contentIdList = ContentIdPrefUtil.loadContentIdList()
//        val tourList = TourItemPrefUtil.loadAllTourItemList()
//
//        val loadedDataSet = mutableListOf<RouteListData>()
//        contentIdList.forEach { contentId ->
//            val tourItem = tourList.find { it.getContentId() == contentId }!!
//            locations.add(
//                LatLng(
//                    tourItem.getLatitude()?.toDouble() ?: 0.0,
//                    tourItem.getLongitude()?.toDouble() ?: 0.0
//                )
//            )
//            Log.d("마커 사이즈 확인", loadedDataSet.size.toString())
//            Log.d("locations", locations.toString())
//            loadedDataSet.add(RouteListData(contentId, tourItem.getTitle(), tourItem.getAddress()))
//        }
//        dataSet = loadedDataSet.toList()
//    }
//
//    private fun initRouteRecyclerView() {
//        adapter.onRouteListDataClickListener = this@RouteFragment
//        binding.rvRouteRecyclerview.adapter = adapter
//        adapter.submitList(dataSet)
//        if (dataSet.isNotEmpty()) {
//            binding.layoutRouteEmptyFrame.visibility = View.INVISIBLE
//        }
//    }
//
//    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
//    override fun onRouteListDataClick(contentId: String) {
//        Log.d(TAG,"fragment)onRouteListDataClick) ${contentId}" )
//        val allTourItemList = TourItemPrefUtil.loadAllTourItemList()
//        val clickedTourItem:TourItem = allTourItemList.find{
//            it.getContentId() == contentId
//        }!!
//        startActivity(
//            PlaceDetailActivity.newIntent(MyApplication.appContext!!, clickedTourItem.getContentTypeId(), clickedTourItem)
//        )
//    }
//
//    private fun initItemTouchSimpleCallback() {
//        val itemTouchHelper = ItemTouchHelper(ItemTouchSimpleCallback())
//        itemTouchHelper.attachToRecyclerView(
//            binding.rvRouteRecyclerview
//        )
//    }
//
//    private fun initRouteFinishButton() {
//        binding.layoutRouteFinishButton.setOnClickListener {
////            val frag = BottomSheetDialog()
//            if (dataSet.isNotEmpty()) {
////                frag.show(childFragmentManager, frag.tag)
//                activity?.let {
//                    val intent = Intent(context, SavePhotoActivity::class.java)
//                    startActivity(intent)
//                }
//            } else Toast.makeText(context, "경로를 추가해 주세요", Toast.LENGTH_SHORT).show()
//        }
//    }
//
//    private fun initToolTip() {
//        //tooltip 버튼
//        binding.ivTooltip.setOnClickListener {
//            val balloon = context?.let { it1 ->
//                createBalloon(it1) {
//                    setWidthRatio(1.0f)
//                    setHeight(BalloonSizeSpec.WRAP)
//                    setText("기록이 저정되며 해당탭이 초기화 됩니다\n저장한 기록은 기록 탭에서 보실 수 있습니다")
//                    setTextColorResource(R.color.black)
//                    setTextSize(15f)
//                    setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
//                    setArrowSize(10)
//                    setArrowPosition(0.5f)
//                    setPadding(12)
//                    setMarginLeft(20)
//                    setMarginRight(20)
//                    setCornerRadius(8f)
//                    elevation
//                    setBackgroundColorResource(R.color.white)
//                    setBalloonAnimation(BalloonAnimation.ELASTIC)
//                    setLifecycleOwner(lifecycleOwner)
//                    build()
//                }
//            }
//            balloon?.showAlignBottom(binding.ivTooltip)
//            Handler(Looper.getMainLooper()).postDelayed({
//                balloon?.dismiss()
//            }, 3000)
//        }
//    }
//
//    override fun onMapReady(naverMap: NaverMap) {
//        this.naverMap = naverMap
//        onMarkerReady()
//    }
//
//    private fun onMarkerReady() {
//        if (locations.isNotEmpty()) {
//            val markerIconBitmap =
//                MapUtils.resizeMapIcons(MyApplication.appContext!!, R.drawable.ic_marker, 120, 120)
//
//            locations.forEach { latLng ->
//                val marker = Marker().apply {
//                    position = latLng
//                    icon = OverlayImage.fromBitmap(markerIconBitmap)
//                    map = naverMap
//                }
//                markers.add(marker) // 마커 리스트에 추가
//            }
//            val bounds = MapUtils.createBoundsForAllMarkers(markers)
////            observeFurthestPairAndConnectMarkers()
//            MapUtils.updateCameraToBounds(naverMap, bounds, 130)
//
//            if (locations.size == 1) {
//                naverMap.moveCamera(CameraUpdate.zoomTo(13.0))
//            }
//        } else {
//            // TODO : locations에 저장된 값이 없을 때 현재 선택한 지역이 카메라에 보이게 수정
//        }
//
//        connectMarkersSequentiallyFromFurthest(naverMap)
//    }
//
//    // 저장된 순서대로 마커끼리 폴리라인 연결하는 함수
//    private fun connectMarkersSequentiallyFromFurthest(naverMap: NaverMap) {
//        if (locations.size > 1) {
//            val markerPositions = locations.map { location ->
//                LatLng(location.latitude, location.longitude)
//            }
//            drawPolyline(naverMap, markerPositions)
//        }
//    }
//
//    override fun onStart() {
//        super.onStart()
//        mapView.onStart()
//    }
//
//    override fun onResume() {
//        initDataSet()
//        adapter.submitList(dataSet)
//
//        mapView.onResume()
//        super.onResume()
//    }
//
//    override fun onPause() {
//        mapView.onPause()
//        super.onPause()
//    }
//
//    override fun onStop() {
//        mapView.onStop()
//        super.onStop()
//    }
//
//    override fun onLowMemory() {
//        super.onLowMemory()
//        mapView.onLowMemory()
//    }
//
//    override fun onSaveInstanceState(outState: Bundle) {
//        super.onSaveInstanceState(outState)
//        mapView.onSaveInstanceState(outState)
//    }
//}


class RouteFragment : Fragment(), OnMapReadyCallback, OnRouteListDataClickListener {
    private val TAG = "RouteFragment"

    private lateinit var binding: FragmentRouteBinding

    private val routeAdapter: RouteAdapter by lazy {
        RouteAdapter()
    }

    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView
    private lateinit var locationSource: FusedLocationSource

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
        initRouteFinishButton()
        initToolTip()
    }

    private fun initModelObserver() {
        routeViewModel.routeListDataSet.observe(viewLifecycleOwner) { routeDataList ->
            routeAdapter.submitList(routeDataList.toMutableList())

            if (routeDataList.isNotEmpty()!!) {
                binding.layoutRouteEmptyFrame.visibility = View.INVISIBLE
            }
        }

        routeViewModel.locations.observe(viewLifecycleOwner) { locations ->
            if (locations.isNotEmpty()) {
                val markerIconBitmap =
                    MapUtils.resizeMapIcons(
                        MyApplication.appContext!!,
                        R.drawable.ic_marker,
                        120,
                        120
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
//            observeFurthestPairAndConnectMarkers()
                MapUtils.updateCameraToBounds(naverMap, bounds, 130)

                if (locations.size == 1) {
                    naverMap.moveCamera(CameraUpdate.zoomTo(13.0))
                }
            } else {
                // TODO : locations에 저장된 값이 없을 때 현재 선택한 지역이 카메라에 보이게 수정
            }

            Log.d("마커 폴리라인 연결", "폴리라인 연결 함수 초반")
            if (locations.size > 1) {
                val markerPositions = locations.map { location ->
                    LatLng(location.latitude, location.longitude)
                }
                markerPositions.forEach {
                    Log.d("마커 폴리라인 위도 경도", "${it.longitude}+${it.latitude}")
                }
                Log.d("마커 폴리라인 연결", markerPositions.size.toString())
                drawPolyline(naverMap, markerPositions)
            }
        }
    }

    private fun initRouteRecyclerView() {
        routeAdapter.onRouteListDataClickListener = this@RouteFragment
        binding.rvRouteRecyclerview.adapter = routeAdapter
    }

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
    } //범위만 바꿔주는 노티파이가 있는데 찾아볼것

    private fun initItemTouchSimpleCallback() {
        val itemTouchHelper = ItemTouchHelper(ItemTouchSimpleCallback())
        itemTouchHelper.attachToRecyclerView(
            binding.rvRouteRecyclerview
        )
    }

    private fun initRouteFinishButton() {
        binding.layoutRouteFinishButton.setOnClickListener {
//            val frag = BottomSheetDialog()
//            if (dataSet.isNotEmpty()) {
//                frag.show(childFragmentManager, frag.tag)
            activity?.let {
                val intent = Intent(context, SavePhotoActivity::class.java)
                startActivity(intent)
            }
//            } else Toast.makeText(context, "경로를 추가해 주세요", Toast.LENGTH_SHORT).show()
        }
    }

    private fun initToolTip() {
        //tooltip 버튼
        binding.ivTooltip.setOnClickListener {
            val balloon = context?.let { it1 ->
                createBalloon(it1) {
                    setWidthRatio(1.0f)
                    setHeight(BalloonSizeSpec.WRAP)
                    setText("기록이 저정되며 해당탭이 초기화 됩니다\n저장한 기록은 기록 탭에서 보실 수 있습니다")
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
        routeViewModel.getLocation()
    }


    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        mapView.onResume()
        super.onResume()
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
    }
}