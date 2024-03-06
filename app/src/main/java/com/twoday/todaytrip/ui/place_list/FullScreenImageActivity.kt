package com.twoday.todaytrip.ui.place_list

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityFullScreenImageBinding
import com.twoday.todaytrip.databinding.FragmentPlaceListBinding

class FullScreenImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenImageBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val imageResource = intent.getIntExtra("imageResource", 0)

        binding.imageViewFullScreen.setImageResource(imageResource)

        binding.imageViewFullScreen.setOnClickListener {
            finish()
        }
    }
}