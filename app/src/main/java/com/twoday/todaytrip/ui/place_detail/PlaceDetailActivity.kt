package com.twoday.todaytrip.ui.place_detail

import android.os.Bundle
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.twoday.todaytrip.databinding.ActivityPlaceDetailBinding

class PlaceDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityPlaceDetailBinding

    private val viewModel: PlaceDetailViewModel by viewModels()

    private val placePhotoAdapter by lazy { PlaceDetailPhotoAdapter() }
    private val placeInfoAdapter by lazy { PlaceDetailExtraInfoAdapter() }
    private val placeMemoryAdapter by lazy { PlaceDetailMyMemoryAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityPlaceDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)
        initView()
    }

    private fun initView() {
        val extraInfoRecyclerView = binding.rvPlaceDetailExtraInfoList
        val myMemoryRecyclerView = binding.rvPlaceDetailMyMemoryList

    }
}