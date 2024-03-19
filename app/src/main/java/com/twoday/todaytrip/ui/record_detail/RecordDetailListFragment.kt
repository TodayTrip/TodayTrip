package com.twoday.todaytrip.ui.record_detail

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRecordDetailListBinding
import com.twoday.todaytrip.utils.MapUtils
import com.twoday.todaytrip.utils.RecordPrefUtil
import com.twoday.todaytrip.viewModel.RecordDetailViewModel

class RecordDetailListFragment : Fragment(), OnMapReadyCallback {

    private val TAG = "RecordDetailListFragment"
    private var _binding: FragmentRecordDetailListBinding? = null
    private val binding get() = _binding!!
    private val recordDetailListAdapter by lazy { RecordDetailListAdapter() }
    private val viewModel: RecordDetailViewModel by activityViewModels()

    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    private val markers = mutableListOf<Marker>()
    private lateinit var markerList: List<LatLng>

    private val recordList = RecordPrefUtil.loadRecordList()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = FragmentRecordDetailListBinding.inflate(layoutInflater)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initModelObserver()

        mapView = binding.mvRecordDetailMap
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        Log.d(TAG, "recordList: $recordList")
        if (recordList.isNotEmpty()) {
            markerList = recordList.flatMap { record ->
                record.savePhotoDataList.map { photoData ->
                    LatLng(
                        photoData.tourItem.getLatitude().toDouble(),
                        photoData.tourItem.getLongitude().toDouble()
                    )
                }
            }
            Log.d(TAG, "locations: $markerList")
        } else {
            Log.d("기록된 경로가 없음", "nothing")
        }

        initImageRecyclerView()
    }

    private fun initImageRecyclerView() {
        binding.rvRecordDetailList.apply {
//            layoutManager = LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            adapter = this@RecordDetailListFragment.recordDetailListAdapter
        }
        Log.d(TAG, "어댑터 $binding.rvRecordDetailList.adapter.toString()")
    }

    private fun initModelObserver() {
        viewModel.marker.observe(viewLifecycleOwner, Observer {
            markerList = it
            Log.d("RecordDetailListFragment","뷰모델에서 받아온 위도 경도: $markerList")
        })
        viewModel.dataSetList.observe(viewLifecycleOwner){ dataSetList ->
            this.recordDetailListAdapter.submitList(dataSetList)
            Log.d(TAG,"옵저브 ${this.recordDetailListAdapter.submitList(dataSetList).toString()}")
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        val markerIconBitmap =
            MapUtils.resizeMapIcons(requireContext(), R.drawable.ic_marker, 120, 120)
        val photoBitmap =
            MapUtils.resizeMapIcons(requireContext(), R.drawable.img_pic_marker, 80, 80)
        val combinedBitmap = MapUtils.combineImages(markerIconBitmap, photoBitmap)

        markerList.forEach { latLng ->
            Log.d("latLng", latLng.toString())
            val marker = Marker().apply {
                position = latLng
                icon = OverlayImage.fromBitmap(combinedBitmap)
                map = naverMap
            }
            markers.add(marker) // 마커 리스트에 추가
        }

        val bounds = MapUtils.createBoundsForAllMarkers(markers)

        // 마커를 추가 한 후 아래 함수를 호출해야 함
//        observeFurthestPairAndConnectMarkers()
        MapUtils.updateCameraToBounds(naverMap, bounds, 450)
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