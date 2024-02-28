package com.twoday.todaytrip.ui.place_map

import android.Manifest
import android.content.pm.PackageManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.content.ContextCompat
import com.naver.maps.map.LocationSource
import com.naver.maps.map.MapFragment
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityPlaceMapBinding
import com.twoday.todaytrip.ui.place_detail.PlaceDetailFragment

class PlaceMapActivity : AppCompatActivity(), OnMapReadyCallback {

    private var _binding: ActivityPlaceMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlaceMapAdapter

    private lateinit var viewModel: PlaceMapViewModel

    private lateinit var map: NaverMap
    private lateinit var locationSource: FusedLocationSource
    private val LOCATION_PERMISSION_REQUEST_CODE = 500

    private val PERMISSIONS = listOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.ACCESS_COARSE_LOCATION
    )

    private lateinit var currentFocusPlace: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlaceMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initView()
        initViewModel()
        setListener()
        setDetailFragment(currentFocusPlace)

    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    override fun onMapReady(p0: NaverMap) {
        TODO("Not yet implemented")
    }

    private fun initView() {

        initMap()
    }

    private fun initViewModel() {

    }

    fun setDetailFragment(placeId: String) = with(binding) {
        val fragment = PlaceDetailFragment()
        fragment.apply {
            arguments = Bundle().apply {
                putString("place_id", placeId)
            }
        }
    }

    private fun setListener() {
        val clickListener = View.OnClickListener {
            if (!isFinishing) finish()
        }
        binding.ivPlaceMapBack.setOnClickListener(clickListener)
        binding.tvPlaceMapBack.setOnClickListener(clickListener)
    }

    private fun initMap() {
        val mapFragment =
            supportFragmentManager.findFragmentById(R.id.fcv_place_map_full_map) as MapFragment?
                ?: MapFragment.newInstance().also {
                    supportFragmentManager.beginTransaction().add(R.id.fcv_place_map_full_map, it)
                        .commit()
                }
        mapFragment.getMapAsync(this)
        locationSource = FusedLocationSource(this, 0)
    }

    private fun checkPermission() {
        for (permission in PERMISSIONS) {
            if (ContextCompat.checkSelfPermission(
                    this,
                    permission
                ) != PackageManager.PERMISSION_GRANTED
            ) {

            } else {

            }
        }

    }
}