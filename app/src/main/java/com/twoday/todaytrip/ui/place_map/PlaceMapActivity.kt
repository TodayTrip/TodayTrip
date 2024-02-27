package com.twoday.todaytrip.ui.place_map

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.LayoutInflater
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityPlaceMapBinding

class PlaceMapActivity : AppCompatActivity() {

    private var _binding: ActivityPlaceMapBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapter: PlaceMapAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityPlaceMapBinding.inflate(layoutInflater)
        setContentView(binding.root)

    }


}