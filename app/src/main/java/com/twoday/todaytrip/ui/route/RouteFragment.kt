package com.twoday.todaytrip.ui.route

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.twoday.todaytrip.databinding.FragmentRouteBinding
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil

class RouteFragment : Fragment(), OnMapReadyCallback {
    private val TAG = "RouteFragment"

    private lateinit var binding: FragmentRouteBinding

    private lateinit var dataSet: List<RouteListData>
    private val adapter: RouteAdapter by lazy {
        RouteAdapter()
    }

    private lateinit var map: NaverMap
    private lateinit var locationSource: FusedLocationSource

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initDataSet()
        initRouteRecyclerView()
        initItemTouchSimpleCallback()
        initRouteFinishButton()
    }

    override fun onResume() {
        initDataSet()
        adapter.submitList(dataSet)
        super.onResume()
    }

    private fun initDataSet() {
        val contentIdList = ContentIdPrefUtil.loadContentIdList()
        val tourList = TourItemPrefUtil.loadAllTourItemList()

        val loadedDataSet = mutableListOf<RouteListData>()
        contentIdList.forEach {contentId ->
            val tourItem = tourList.find { it.getContentId() == contentId }!!
            loadedDataSet.add(RouteListData(tourItem.getTitle(), tourItem.getAddress()))
        }
        dataSet = loadedDataSet.toList()
    }

    private fun initRouteRecyclerView() {
        binding.rvRouteRecyclerview.adapter = adapter
        adapter.submitList(dataSet)
        if (dataSet.isNotEmpty()) {
            binding.layoutRouteEmptyFrame.visibility = View.INVISIBLE
        }
    }

    private fun initItemTouchSimpleCallback(){
        val itemTouchHelper = ItemTouchHelper(ItemTouchSimpleCallback())
        itemTouchHelper.attachToRecyclerView(
            binding.rvRouteRecyclerview
        )
    }

    private fun initRouteFinishButton() {
        binding.btnRouteFinish.setOnClickListener {
            val frag = BottomSheetDialog()
            frag.show(childFragmentManager, frag.tag)
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.map = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Face
    }
}