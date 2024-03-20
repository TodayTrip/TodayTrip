package com.twoday.todaytrip.ui.save_photo

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolylineOverlay
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.FishBun.Companion.INTENT_PATH
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivitySavePhotoBinding
import com.twoday.todaytrip.ui.place_map.LocationInfo
import com.twoday.todaytrip.ui.route.RecordBottomSheetDialog
import com.twoday.todaytrip.utils.MapUtils
import com.twoday.todaytrip.viewModel.SavePhotoViewModel


class SavePhotoActivity : AppCompatActivity(), OnMapReadyCallback {
    private val TAG = "SavePhotoActivity"

    private val binding: ActivitySavePhotoBinding by lazy {
        ActivitySavePhotoBinding.inflate(layoutInflater)
    }

    private val adapter: SavePhotoAdapter by lazy {
        SavePhotoAdapter()
    }
    val savePhotoDataList = mutableListOf<SavePhotoData>()
    private var position = 0
    private val savePhotoViewModel by viewModels<SavePhotoViewModel>()

    private lateinit var naverMap: NaverMap
    private lateinit var mapView: MapView

    private var markerList: MutableList<LocationInfo> = mutableListOf()
    private val markers = mutableListOf<Marker>()

    private val polylineOverlay = PolylineOverlay()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        // mapView 변수 초기화
        mapView = binding.mvRoute
        mapView.onCreate(savedInstanceState)
        mapView.getMapAsync(this)

        initModelObserver()
        initSavePhotoDataList()
        initSavePhotoRecylerView()
        initRouteFinishButton()
    }

    private fun initModelObserver() {
        savePhotoViewModel.savePhotoDataList.observe(this, Observer { savePhotoList ->
            adapter.submitList(savePhotoList)
            savePhotoList.forEach {
                markerList.add(
                    LocationInfo(
                        LatLng(
                            it.tourItem.getLatitude().toDouble(),
                            it.tourItem.getLongitude().toDouble()
                        ),
                        it.tourItem.getTitle()
                    )
                )
            }
        })
    }

    private fun initSavePhotoDataList() {
        savePhotoViewModel.savePhotoData()
    }

    private fun initSavePhotoRecylerView() {
        adapter.submitList(savePhotoDataList)
        binding.rvSavephotoRecyclerview.adapter = adapter

        adapter.itemClick = object : SavePhotoAdapter.ItemClick {
            override fun onClick(item: SavePhotoData, position: Int) {
                this@SavePhotoActivity.position = position
                FishBun.with(this@SavePhotoActivity).setImageAdapter(GlideAdapter())
                    .startAlbumWithOnActivityResult(FishBun.FISHBUN_REQUEST_CODE)
                Toast.makeText(this@SavePhotoActivity, "클릭", Toast.LENGTH_SHORT).show()
            }
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        when (requestCode) {
            FishBun.FISHBUN_REQUEST_CODE -> if (resultCode == RESULT_OK) {
                val path = data?.getParcelableArrayListExtra<Parcelable>(INTENT_PATH)
                if (!path.isNullOrEmpty()) {
                    val count = path.size
                    val imageList: MutableList<String> = mutableListOf()
                    for (index in 0 until count!!) {
                        val imageUri = path[index]
                        Log.d("TAG", "${imageUri.toString()}")
                        imageList.add(imageUri.toString())
                    }
                    adapter.addImagesUriList(imageList, position)
                    savePhotoViewModel.addImage(position, imageList)
//                adapter.addImageUri(imageList[0], position)
                }
            }
        }
    }

    private fun initRouteFinishButton() {
        binding.btnRouteFinish.setOnClickListener {
            val frag = RecordBottomSheetDialog()
            frag.show(supportFragmentManager, frag.tag)
            Log.d("SavePhotoActivity 토스트", "토스트")
        }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.naverMap = naverMap

        naverMap.uiSettings.isZoomControlEnabled = false
        initDrawMarker()
    }

    private fun initDrawMarker() {
        markerList.forEachIndexed { index, latLng ->
            val text = (index + 1).toString()
            val iconWithTextBitmap =
                MapUtils.createIconWithText(
                    this,
                    R.drawable.ic_blue_marker,
                    text
                )
            val resizedIconBitmap =
                MapUtils.resizeBitmap(
                    iconWithTextBitmap,
                    90,
                    90
                )
            val marker = Marker().apply {
                position = latLng.latLng
                icon = OverlayImage.fromBitmap(resizedIconBitmap)
                map = naverMap
            }
            markers.add(marker)
        }
        val bounds = MapUtils.createBoundsForAllMarkers(markers)

        MapUtils.updateCameraToBounds(naverMap, bounds, 130)

        if (markerList.size > 1) {
            val markerPositions = markerList.map { location ->
                LatLng(location.latLng.latitude, location.latLng.longitude)
            }
            polylineOverlay.map = null
            polylineOverlay.apply {
                coords = markerPositions
                color = 0xFF0085FF.toInt()
                map = naverMap
            }
        } else {
            naverMap.moveCamera(CameraUpdate.zoomTo(13.0))
        }
    }

    override fun onStart() {
        super.onStart()
        mapView.onStart()
    }

    override fun onResume() {
        mapView.onResume()
        mapView.getMapAsync(this)
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