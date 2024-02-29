package com.twoday.todaytrip.ui.place_map

import android.Manifest
import android.content.pm.PackageManager
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraPosition
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentPlaceMapBinding

class PlaceMapFragment : Fragment(), OnMapReadyCallback {

    companion object {
        fun newInstance() = PlaceMapFragment()
    }

    private var _binding: FragmentPlaceMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: PlaceMapViewModel

    private lateinit var map: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE = 500

    private val PERMISSIONS = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_place_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
        initViewModel()
        setListener()
    }
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(PlaceMapViewModel::class.java)
        // TODO: Use the ViewModel
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.map = naverMap
//        naverMap.locationSource = locationSource
//        naverMap.uiSettings.isLocationButtonEnabled = true
//        naverMap.locationTrackingMode = LocationTrackingMode.Face
        naverMap.cameraPosition = CameraPosition(LatLng(20.38, 150.55), 9.0)
    }

    private fun initView() {

        initMap()
    }

    private fun initViewModel() {

    }

    private fun setListener() {
        val clickListener = View.OnClickListener {
            childFragmentManager.popBackStack()
        }
    }

    private fun initMap() {
        val mapFragment =
            childFragmentManager.findFragmentById(R.id.fcv_place_map_full_map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    childFragmentManager.beginTransaction().add(R.id.fcv_place_map_full_map, it)
                        .commit()
                }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE)
    }

    private fun checkPermission() {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    requireContext(),
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            } else {

            }
        }
    }

}