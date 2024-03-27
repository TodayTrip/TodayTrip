package com.twoday.todaytrip.ui.record_gallery

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityFullScreenImageBinding

class FullScreenImageActivity : AppCompatActivity() {
    private lateinit var binding: ActivityFullScreenImageBinding
    private lateinit var viewPager: ViewPager2

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityFullScreenImageBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewPager = binding.viewPager

        binding.btnGalleryBack.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
        }

        // URI 목록을 받아옴
        val imageUris = intent.getStringArrayListExtra("imageUris")
        val position = intent.getIntExtra("position", 0)

        // 어댑터 설정
        imageUris?.let {
            val adapter = ImagePagerAdapter(it)
            viewPager.adapter = adapter
            binding.viewPager.setCurrentItem(position, false)
        }
    }
}
