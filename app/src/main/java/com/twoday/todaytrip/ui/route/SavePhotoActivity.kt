package com.twoday.todaytrip.ui.route

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.net.toUri
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ActivitySavePhotoBinding
import com.twoday.todaytrip.ui.RecordActivity


class SavePhotoActivity : AppCompatActivity() {
    private val TAG = "SavePhotoActivity"

    private val binding: ActivitySavePhotoBinding by lazy {
        ActivitySavePhotoBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: SavePhotoAdapter

    private val datalist = mutableListOf<SavePhotoData>()
    private val currentList: MutableList<Uri> = mutableListOf()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        adapter = SavePhotoAdapter(datalist)
        binding.rvSavephotoRecyclerview.adapter = adapter

        adapter.itemClick = object : SavePhotoAdapter.ItemClick {
            override fun onClick(item: SavePhotoData, position: Int) {
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                activityResult.launch(intent)
                Toast.makeText(this@SavePhotoActivity, "클릭", Toast.LENGTH_SHORT).show()
            }
        }

        binding.layoutRouteFinishButton.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }
    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val uri = it.data!!.data!!
            Glide.with(this).load(uri).into(binding.imgLoad)
        }
    }
}