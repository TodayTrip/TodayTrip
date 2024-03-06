package com.twoday.todaytrip.ui.route

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRouteBinding
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.MapUtils
import com.twoday.todaytrip.utils.MapUtils.drawPolyline
import com.twoday.todaytrip.utils.TourItemPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil.loadTouristAttractionList

class RouteFragment : Fragment(), OnMapReadyCallback {

    private var _binding: FragmentRouteBinding? = null
    private val binding get() = _binding!!

    private lateinit var tourList: List<TourItem>
    private val itemTouchSimpleCallback = ItemTouchSimpleCallback()
    private val itemTouchHelper = ItemTouchHelper(itemTouchSimpleCallback)

    private val adapter: RouteAdapter by lazy { RouteAdapter() }

    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    private val markers = mutableListOf<Marker>()

    private var locations: MutableList<LatLng> = mutableListOf()

    private val viewModel by lazy {
        ViewModelProvider(this@RouteFragment)[RouteViewModel::class.java]
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        // mapView 변수 초기화
        mapView = binding.mvRoute
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initializeItemList()
        initRecyclerView()
        initClickListener()
        initCallBack()
    }


    private fun initializeItemList() {
        val tour = loadTouristAttractionList()
        val rest = TourItemPrefUtil.loadRestaurantList()
        val cafe = TourItemPrefUtil.loadCafeList()
        val event = TourItemPrefUtil.loadEventList()
        tourList = tour + rest + cafe + event
    }

    private fun initRecyclerView() {
        val contentIdList = ContentIdPrefUtil.loadContentIdList()
        val dataSet: MutableList<RouteListData> = mutableListOf()

        //순서 변경하면 변경사항 저장되게하기
        //담은 아이템 RecyclerView에 띄우기
        if(contentIdList.isNotEmpty()) {
            var count = 0
            contentIdList.forEach {
                val place = tourList.find { it.getContentId() == contentIdList[count] }
                locations.add(
                    LatLng(
                        place?.getLatitude()?.toDouble() ?: 0.0,
                    place?.getLongitude()?.toDouble() ?: 0.0
                ))
                dataSet.add(RouteListData(place?.getTitle() ?: "", place?.getAddress() ?: ""))
                if (contentIdList.size-1 > count) {
                    count += 1
                }
            }
        }

        adapter.submitList(dataSet)
        binding.rvRouteRecyclerview.adapter = adapter
        //리싸이클러뷰에 아무것도 없을시 아이콘 띄움
        if (dataSet.isNotEmpty()){
            binding.layoutRouteEmptyFrame.visibility = View.INVISIBLE
        }

        // itemTouchHelper와 recyclerview 연결, 아이템 순서변경
        itemTouchHelper.attachToRecyclerView(binding.rvRouteRecyclerview)
    }


    private fun initClickListener() {
        binding.btnRouteFinish.setOnClickListener {
            val frag = BottomSheetDialog()
            frag.show(childFragmentManager, frag.tag)
        }
    }

    private fun initCallBack() {
        itemTouchSimpleCallback.setOnItemMoveListener(object :
            ItemTouchSimpleCallback.OnItemMoveListener {
            override fun onItemMove(from: Int, to: Int) {
                binding.rvRouteRecyclerview.adapter = adapter

            }
        })

        adapter.itemClick = object : RouteAdapter.ItemClick {
            override fun onClick(item: RouteListData) {

                Log.d("favoritefragment", "remove  ${item}")
            }
        }
    }

    //viewModel 사용하기 위한 observe 함수 모음
//    private fun observeFurthestPairAndConnectMarkers() {
////        viewModel.findFurthestMarkers(markers) // LiveData를 업데이트하도록 요청
////        viewModel.furthestPair.observe(viewLifecycleOwner, Observer { furthestPair ->
////            furthestPair?.let {
////                connectMarkersSequentiallyFromFurthest(naverMap)
////            }
////        })
//    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        onMarkerReady()
    }

    private fun onMarkerReady() {

        if(locations.isNotEmpty()){
            val markerIconBitmap =
                MapUtils.resizeMapIcons(requireContext(), R.drawable.ic_marker, 120, 120)

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

            if(locations.size == 1){
                naverMap.moveCamera(CameraUpdate.zoomTo(13.0))
            }
        } else {
            // TODO : locations에 저장된 값이 없을 때 현재 선택한 지역이 카메라에 보이게 수정
        }

        connectMarkersSequentiallyFromFurthest(naverMap)
    }

    // 저장된 순서대로 마커끼리 폴리라인 연결하는 함수
    private fun connectMarkersSequentiallyFromFurthest(naverMap: NaverMap) {
        if (locations.size > 1) {
            val markerPositions = locations.map { location ->
                LatLng(location.latitude, location.longitude)
            }
            drawPolyline(naverMap, markerPositions)
        }
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
    }

    override fun onDestroyView() {
        super.onDestroyView()
        mapView.onDestroy()
        _binding = null
    }

}