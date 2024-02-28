package com.twoday.todaytrip.ui.route

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.naver.maps.map.LocationSource
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRouteBinding

class RouteFragment : Fragment(), OnMapReadyCallback {

    private lateinit var adapter: RouteAdapter
    private lateinit var binding: FragmentRouteBinding

    private lateinit var map: NaverMap
    private lateinit var locationSource: FusedLocationSource

    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
//            param1 = it.getString(ARG_PARAM1)
//            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val datalist = mutableListOf<RouteListData>()
        datalist.add(RouteListData("서울역",1))
        datalist.add(RouteListData("N서울 타워",2))
        datalist.add(RouteListData("청계천",3))
        datalist.add(RouteListData("북촌 한옥 마을",4))
        datalist.add(RouteListData("경로",5))
        datalist.add(RouteListData("경로",6))

        adapter = RouteAdapter(datalist)
        binding.rvReouteRecyclerview.adapter = adapter

        binding.cvRouteEditFrame.setOnClickListener{

        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RouteFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.map = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Face
    }
}