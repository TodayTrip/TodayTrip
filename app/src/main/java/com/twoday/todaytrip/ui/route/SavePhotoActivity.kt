package com.twoday.todaytrip.ui.route

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.R
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil.load
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ActivitySavePhotoBinding
import com.twoday.todaytrip.ui.RecordActivity


class SavePhotoActivity : AppCompatActivity() {

    private val binding: ActivitySavePhotoBinding by lazy {
        ActivitySavePhotoBinding.inflate(layoutInflater)
    }

        private lateinit var adapter: SavePhotoAdapter
//    private val adapter: SavePhotoAdapter by lazy {
//        SavePhotoAdapter()
//    }

    private val datalist = mutableListOf<SavePhotoData>()
    private val currentList: MutableList<Uri> = mutableListOf()





    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)


//        val datalist = mutableListOf<SavePhotoData>()
        datalist.add(
            SavePhotoData(
                "".toUri(),
                "서울역",
                "어딘가"
            )
        )
        datalist.add(
            SavePhotoData(
                "".toUri(),
                "N서울 타워",
                "있겠지"
            )
        )
        datalist.add(
            SavePhotoData(
                "".toUri(),
                "청계천",
                "아무데나"
            )
        )
        datalist.add(
            SavePhotoData(
                "".toUri(),
                "북촌 한옥 마을",
                "가볼까"
            )
        )
        datalist.add(
            SavePhotoData(
                "".toUri(),
                "경로",
                "어디든지"
            )
        )
        datalist.add(
            SavePhotoData(
                "".toUri(),
                "경로",
                "떠나자"
            )
        )


        adapter = SavePhotoAdapter(datalist)
        binding.rvSavephotoRecyclerview.adapter = adapter

        adapter.itemClick = object : SavePhotoAdapter.ItemClick {
            override fun onClick(item: SavePhotoData,position: Int) {

                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                activityResult.launch(intent)
                val uridata = currentList.get(0)
                datalist[position] = SavePhotoData(uridata,"d","d")
                Toast.makeText(this@SavePhotoActivity, "클릭", Toast.LENGTH_SHORT).show()
            }
        }

        binding.layoutRouteFinishButton.setOnClickListener {
            val intent = Intent(this, RecordActivity::class.java)
            startActivity(intent)
        }

    }

    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && it.data != null) {
            val uri = it.data!!.data
            Glide.with(this).
                    load(uri).into(binding.imgLoad)
            uri?.let { it1 -> currentList.add(0, it1) }
        }
    }

}