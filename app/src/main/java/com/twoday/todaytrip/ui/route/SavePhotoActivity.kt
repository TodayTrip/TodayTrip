package com.twoday.todaytrip.ui.route

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.R
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ActivitySavePhotoBinding


class SavePhotoActivity : AppCompatActivity() {

    private val binding: ActivitySavePhotoBinding by lazy {
        ActivitySavePhotoBinding.inflate(layoutInflater)
    }

    //    private lateinit var adapter: SavePhotoAdapter
    private val adapter: SavePhotoAdapter by lazy {
        SavePhotoAdapter()
    }

//    val PERMISSIONS_REQUEST_CODE = 100
//    var REQUIRED_PERMISSIONS = arrayOf<String>( READ_EXTERNAL_STORAGE)
//
//    val REQUEST_GET_IMAGE = 105
//    var positionMain = 0
//    lateinit var itemMain:SavePhotoData
//    var dataList:MutableList<SavePhotoData> = mutableListOf()
//    lateinit var recyclerAdapter : SavePhotoAdapter
//
//    override fun onRequestPermissionsResult(
//        requestCode: Int, permissions: Array<out String>, grantResults: IntArray
//    ) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
//        when(requestCode){
//            PERMISSIONS_REQUEST_CODE -> {
//                if(grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                    //권한 허용
//                }else{
//                    //권한 거부됨
//                }
//                return
//            }
//        }
//    }
//
//    private fun requestPermission(){
//        var permissionCheck = ContextCompat.checkSelfPermission(this, READ_EXTERNAL_STORAGE)
//        if(permissionCheck != PackageManager.PERMISSION_GRANTED){
//            //설명이 필요한지
//            if(ActivityCompat.shouldShowRequestPermissionRationale(this, READ_EXTERNAL_STORAGE)){
//                //설명 필요 (사용자가 요청을 거부한 적이 있음)
//                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
//            }else{
//                //설명 필요하지 않음
//                ActivityCompat.requestPermissions(this, REQUIRED_PERMISSIONS, PERMISSIONS_REQUEST_CODE )
//            }
//        }else{
//            //권한 허용
//        }
//    }
//
//    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//        super.onActivityResult(requestCode, resultCode, data)
//        if(resultCode == Activity.RESULT_OK){
//            when(requestCode){
//                REQUEST_GET_IMAGE -> {
//                    try{
//                        var uri = data?.data
//                        dataList[positionMain].image = uri.toString()
//                        recyclerAdapter.notifyDataSetChanged()
//                    }catch (e:Exception){}
//                }
//            }
//        }
//    }

//    private val permissionList = arrayOf(READ_EXTERNAL_STORAGE)
//    private val checkPermission =
//        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { result ->
//            result.forEach {
//                if (!it.value) {
//                    Toast.makeText(applicationContext, "권한 동의 필요!", Toast.LENGTH_SHORT).show()
//                    finish()
//                }
//            }
//        }
//    private val readImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
//        img_load.load(uri)
//    }


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        checkPermission.launch(permissionList)


//        requestPermission()

        val datalist = mutableListOf<SavePhotoData>()
        datalist.add(
            SavePhotoData(
                "data:image/png;base64,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",
                "서울역",
                1,
                "어딘가"
            )
        )
        datalist.add(
            SavePhotoData(
                "data:image/png;base64,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",
                "N서울 타워",
                2,
                "있겠지"
            )
        )
        datalist.add(
            SavePhotoData(
                "data:image/png;base64,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",
                "청계천",
                3,
                "아무데나"
            )
        )
        datalist.add(
            SavePhotoData(
                "data:image/png;base64,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",
                "북촌 한옥 마을",
                4,
                "가볼까"
            )
        )
        datalist.add(
            SavePhotoData(
                "data:image/png;base64,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",
                "경로",
                5,
                "어디든지"
            )
        )
        datalist.add(
            SavePhotoData(
                "data:image/png;base64,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",
                "경로",
                6,
                "떠나자"
            )
        )


        adapter.submitList(datalist)
        binding.rvSavephotoRecyclerview.adapter = adapter

        adapter.itemClick = object : SavePhotoAdapter.ItemClick {
            override fun onClick(item: SavePhotoData) {
//                readImage.launch("image/*")
//                val intent = Intent(Intent.ACTION_PICK)
//                intent.type = "image/*"
//                startActivityForResult(intent, REQUEST_GET_IMAGE)
                Toast.makeText(this@SavePhotoActivity, "클릭", Toast.LENGTH_SHORT).show()
            }
        }

//        recyclerAdapter = RecyclerAdapter(this, dataList){ position, item ->
//            positionMain = position
//            itemMain = item
//
//            val intent = Intent(Intent.ACTION_PICK)
//            intent.type = "image/*"
//            startActivityForResult(intent, REQUEST_GET_IMAGE)
//        }
//        main_recyclerVIew.adapter = recyclerAdapter
//        main_recyclerVIew.layoutManager = LinearLayoutManager(this)
    }
}