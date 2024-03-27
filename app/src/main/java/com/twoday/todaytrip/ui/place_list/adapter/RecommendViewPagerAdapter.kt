package com.twoday.todaytrip.ui.place_list.adapter

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.naver.maps.geometry.LatLng
import com.naver.maps.map.CameraUpdate
import com.naver.maps.map.MapView
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.overlay.Marker
import com.naver.maps.map.overlay.OverlayImage
import com.naver.maps.map.overlay.PolylineOverlay
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ItemPlaceListRecommendBinding
import com.twoday.todaytrip.databinding.ItemPlaceListRecommendCoverBinding
import com.twoday.todaytrip.databinding.ItemPlaceListRecommendMapBinding
import com.twoday.todaytrip.ui.place_list.RecommendCover
import com.twoday.todaytrip.ui.place_list.RecommendData
import com.twoday.todaytrip.ui.place_list.RecommendEmpty
import com.twoday.todaytrip.ui.place_list.RecommendMap
import com.twoday.todaytrip.ui.place_list.RecommendTourItem
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.utils.DestinationPrefUtil
import com.twoday.todaytrip.utils.MapUtils
import java.lang.Integer.max
import java.lang.Integer.min

enum class RecommendViewType(val viewType: Int) {
    COVER(0),
    TOUR_ITEM_OR_EMPTY(1),
    MAP(2)
}

interface OnRefreshRecommendClickListener {
    fun onRefreshRecommendClick()
}

interface OnAddAllRecommendClickListener {
    fun onAddAllRecommendClick(optimizedOrder: List<Int>)
    fun onMoveToRouteClick()
}

class RecommendViewPagerAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val TAG = "RecommendViewPagerAdapter"

    private var recommendDataList = listOf<RecommendData>()
    private val recommendMapIndex = 5

    var onRefreshRecommendClickListener: OnRefreshRecommendClickListener? = null
    var onTourItemClickListener: OnTourItemClickListener? = null
    var onAddAllRecommendClickListener: OnAddAllRecommendClickListener? = null

    override fun getItemViewType(position: Int): Int {
        return when (recommendDataList[position % 6]) {  //6추가
            is RecommendCover -> RecommendViewType.COVER.viewType
            is RecommendMap -> RecommendViewType.MAP.viewType
            else -> RecommendViewType.TOUR_ITEM_OR_EMPTY.viewType
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            RecommendViewType.COVER.viewType -> {
                val binding = ItemPlaceListRecommendCoverBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                CoverHolder(binding)
            }

            RecommendViewType.MAP.viewType -> {
                val binding = ItemPlaceListRecommendMapBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                MapHolder(binding)
            }

            else -> {
                val binding = ItemPlaceListRecommendBinding.inflate(
                    LayoutInflater.from(parent.context), parent, false
                )
                Holder(binding)
            }
        }
    }

    override fun getItemCount(): Int = Int.MAX_VALUE

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        Log.d(TAG, "onBindViewHolder) called, position: ${position % 6}")
        when (val currentRecommendData = recommendDataList[position % 6]) {  //6추가
            is RecommendCover -> {
                (holder as CoverHolder).run {
                    bindCover(currentRecommendData)
                    setOnClickListener()
                }
            }

            is RecommendTourItem -> {
                (holder as Holder).run {
                    bindTourItem(currentRecommendData)
                    setOnClickListener(currentRecommendData)
                }
            }

            is RecommendEmpty -> {
                (holder as Holder).bindEmpty(currentRecommendData)
            }

            is RecommendMap -> {
                (holder as MapHolder).run {
                    bindMap(currentRecommendData)
                    setOnClickListener(currentRecommendData)
                }
            }
        }
    }

    fun changeDataSet(newRecommendDataList: List<RecommendData>) {
        recommendDataList = newRecommendDataList
        notifyDataSetChanged()
    }

    fun changeDataSet(newRecommendDataList: List<RecommendData>, position: Int) {
        recommendDataList = newRecommendDataList
        notifyItemRangeChanged(max(0, position - 6), min(position + 6, itemCount))
    }

    fun getDataSet(): List<RecommendData> = recommendDataList

    inner class CoverHolder(binding: ItemPlaceListRecommendCoverBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.ivItemPlaceListRecommendCoverImage
        private val destinationTextView: TextView = binding.tvItemPlaceListRecommendCoverDestination
        private val refreshLayout = binding.layoutItemPlaceListRecommendCoverRefresh
        fun bindCover(recommendCover: RecommendCover) {
            Log.d(TAG, "bindCover) called")
            //glide util로 바꾸기
            Glide.with(itemView.context)
                .load(recommendCover.imageId)
                .into(imageView)
//            imageView.setImageResource(recommendCover.imageId)
            destinationTextView.text = recommendCover.destination
        }

        fun setOnClickListener() {
            refreshLayout.setOnClickListener {
                Log.d(TAG, "refresh clicked")
                onRefreshRecommendClickListener?.onRefreshRecommendClick()
            }
        }

    }

    inner class Holder(binding: ItemPlaceListRecommendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val imageView: ImageView = binding.ivItemPlaceListRecommendImage
        private val subTitleTextView: TextView = binding.tvItemPlaceListRecommendSubTitle
        private val titleTextView: TextView = binding.tvItemPlaceListRecommendTitle
        private val noResultImageView: ImageView = binding.ivItemPlaceListRecommendNoResult

        fun bindTourItem(recommendTourItem: RecommendTourItem) {
            Log.d(TAG, "bindTourItem) title: ${recommendTourItem.tourItem.getTitle()}")
            recommendTourItem.tourItem.getImage().let { url ->
                Glide.with(itemView.context)
                    .load(url)
                    .into(imageView)
            }
            subTitleTextView.setText(recommendTourItem.subTitleId)
            titleTextView.text = recommendTourItem.tourItem.getTitle()
            noResultImageView.isVisible = false
        }

        fun bindEmpty(recommendEmpty: RecommendEmpty) {
            Log.d(TAG, "bindEmpty) title: ${itemView.context.getString(recommendEmpty.titleId)}")
            imageView.setImageDrawable(null)
            subTitleTextView.setText(recommendEmpty.subTitleId)
            titleTextView.setText(recommendEmpty.titleId)
            Glide.with(itemView.context)
                .load(R.drawable.gif_loading_reading_glasses)
                .into(noResultImageView)
            noResultImageView.isVisible = true
        }

        fun setOnClickListener(recommendTourItem: RecommendTourItem) {
            itemView.setOnClickListener {
                Log.d(TAG, "${recommendTourItem.tourItem.getTitle()} is clicked")
                onTourItemClickListener?.onTourItemClick(recommendTourItem.tourItem)
            }
        }
    }

    inner class MapHolder(binding: ItemPlaceListRecommendMapBinding) :
        RecyclerView.ViewHolder(binding.root), OnMapReadyCallback {

        private lateinit var naverMap: NaverMap
        private val polylineOverlay = PolylineOverlay()
        private val markers = mutableListOf<Marker>()

        private lateinit var optimizedLocations: List<LatLng>
        private lateinit var optimizedOrder: List<Int>

        private val mapView: MapView = binding.mapItemPlaceListRecommendMap
        private val destinationTextView: TextView = binding.tvItemPlaceListRecommendMapDestination
        private val addAllButton: TextView = binding.tvItemPlaceListRecommendMapAddAll
        fun bindMap(recommendMap: RecommendMap) {
            Log.d(TAG, "bindMap) called")
            optimizedLocations = recommendMap.optimizedLocations
            optimizedOrder = recommendMap.optimizedOrder

            clearMap()
            startMapLifecycle()
            mapView.getMapAsync(this@MapHolder)

            destinationTextView.text = recommendMap.destination
            setAllAddButtonUI(recommendMap.isAllAdded)
        }

        private fun clearMap() {
            polylineOverlay.map = null
            markers.forEach {
                it.map = null
            }
            markers.clear()
        }

        private fun setAllAddButtonUI(isAllAdded: Boolean) {
            addAllButton.run {
                backgroundTintList = itemView.context.getColorStateList(
                    if (isAllAdded) R.color.white
                    else R.color.main_blue
                )
                setText(
                    if (isAllAdded) R.string.place_list_recommend_add_all_move
                    else R.string.place_list_recommend_add_all
                )
                setTextColor(
                    itemView.resources.getColor(
                        if (isAllAdded) R.color.place_title_gray
                        else R.color.white
                    )
                )
            }
        }

        fun setOnClickListener(recommendMap: RecommendMap) {
            addAllButton.setOnClickListener {
                if (recommendMap.isAllAdded) {
                    onAddAllRecommendClickListener?.onMoveToRouteClick()
                } else {
                    onAddAllRecommendClickListener?.onAddAllRecommendClick(recommendMap.optimizedOrder)
                    recommendMap.isAllAdded = true
                    setAllAddButtonUI(true)
                }
            }
        }

        override fun onMapReady(naverMap: NaverMap) {
            Log.d(TAG, "onMapReady) called")
            this.naverMap = naverMap
            naverMap.uiSettings.isZoomControlEnabled = false

            if (optimizedLocations.isNotEmpty()) {
                optimizedLocations.forEachIndexed { index, latLng ->
                    val text = (index + 1).toString()
                    val iconWithTextBitmap =
                        MapUtils.createIconWithText(
                            itemView.context,
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
                        position = latLng
                        icon = OverlayImage.fromBitmap(resizedIconBitmap)
                        map = naverMap
                    }
                    markers.add(marker)
                }
                val bounds = MapUtils.createBoundsForAllMarkers(markers)
                MapUtils.updateCameraToBounds(naverMap, bounds, 130)

                if (optimizedLocations.size == 1) {
                    naverMap.moveCamera(CameraUpdate.zoomTo(13.0))
                }
            } else {
                val destination = DestinationPrefUtil.loadDestination()
                val location = DestinationData.destinationLatLng[destination]
                val cameraUpdate = location?.let { CameraUpdate.scrollTo(it) }
                if (cameraUpdate != null) {
                    naverMap.moveCamera(cameraUpdate)
                }
                naverMap.moveCamera(CameraUpdate.zoomTo(9.0))
            }
            if (optimizedLocations.size > 1) {
                val markerPositions = optimizedLocations.map { location ->
                    LatLng(location.latitude, location.longitude)
                }
                polylineOverlay.apply {
                    coords = markerPositions
                    color = 0xFF0085FF.toInt()
                    map = naverMap
                }
            }
        }

        fun startMapLifecycle() {
            Log.d(TAG, "startMapLifecycle) called")
            mapView.onCreate(null)
            mapView.onStart()
            mapView.onResume()
        }
    }
}