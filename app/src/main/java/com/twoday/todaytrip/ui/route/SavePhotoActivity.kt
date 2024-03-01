package com.twoday.todaytrip.ui.route

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityMainBinding
import com.twoday.todaytrip.databinding.ActivitySavePhotoBinding

class SavePhotoActivity : AppCompatActivity() {

    private val binding: ActivitySavePhotoBinding by lazy {
        ActivitySavePhotoBinding.inflate(layoutInflater)
    }
    private lateinit var adapter: SavePhotoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        val datalist = mutableListOf<RouteListData>()
        datalist.add(RouteListData("서울역", 1,"어딘가"))
        datalist.add(RouteListData("N서울 타워", 2,"있겠지"))
        datalist.add(RouteListData("청계천", 3,"아무데나"))
        datalist.add(RouteListData("북촌 한옥 마을", 4,"가볼까"))
        datalist.add(RouteListData("경로", 5,"어디든지"))
        datalist.add(RouteListData("경로", 6,"떠나자"))

        adapter = SavePhotoAdapter(datalist)
        binding.rvSavephotoRecyclerview.adapter = adapter
    }
}