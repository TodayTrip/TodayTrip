package com.twoday.todaytrip.ui.save_photo

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.Observer
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.FishBun.Companion.INTENT_PATH
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivitySavePhotoBinding
import com.twoday.todaytrip.ui.record.Record
import com.twoday.todaytrip.ui.route.RecordBottomSheetDialog
import com.twoday.todaytrip.ui.route.RouteAdapter
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.RecordPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil
import com.twoday.todaytrip.viewModel.SavePhotoViewModel


class SavePhotoActivity : AppCompatActivity() {
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


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initModelObserver()
        initSavePhotoDataList()
        initSavePhotoRecylerView()
        initRouteFinishButton()

    }

    private fun initModelObserver(){
        savePhotoViewModel.savePhotoDataList.observe(this, Observer{savePhotoList ->
            adapter.submitList(savePhotoList)
        })
    }

    private fun initSavePhotoDataList() {
        savePhotoViewModel.savePhotoData()
//        val allTourItemList = TourItemPrefUtil.loadAllTourItemList()
//        val addedContentIdList = ContentIdPrefUtil.loadContentIdList()
//
//        addedContentIdList.forEach { contentID ->
//            val tourItem = allTourItemList.find {
//                it.getContentId() == contentID
//            }!!
//            savePhotoDataList.add(SavePhotoData(tourItem))
//            if (allTourItemList.isNotEmpty()) {
////                binding.layoutRouteEmptyFrame.visibility = View.INVISIBLE
//            }
//        }
    }

    private fun initSavePhotoRecylerView() {
        adapter.submitList(savePhotoDataList)
        binding.rvSavephotoRecyclerview.adapter = adapter

        adapter.itemClick = object : SavePhotoAdapter.ItemClick {
            override fun onClick(item: SavePhotoData, position: Int) {
                this@SavePhotoActivity.position = position
                FishBun.with(this@SavePhotoActivity).setImageAdapter(GlideAdapter())
                    .startAlbumWithOnActivityResult(FishBun.FISHBUN_REQUEST_CODE)
//                val intent = Intent(Intent.ACTION_PICK)
//                intent.type = "image/*"
//                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true) //멀티선택기능
//                activityResult.launch(intent)
                Toast.makeText(this@SavePhotoActivity, "클릭", Toast.LENGTH_SHORT).show()
            }
        }
    }

//    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()) {
//        if (it.resultCode == RESULT_OK && it.data != null) {
////            uri = it.data!!.data!!
////            grantUriPermission(
////                "com.twoday.todaytrip",
////                uri,
////                Intent.FLAG_GRANT_READ_URI_PERMISSION
////            )
////            Glide.with(this).
////                    load(uri).into(binding.imgLoad)
//            binding.imgLoad.load(uri)
////            uri?.let { it1 -> currentList.add(0, it1) }
//        }
//    }
//
////    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
////        ActivityResultContracts.StartActivityForResult()
////    ) {
//        override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
//            super.onActivityResult(requestCode, resultCode, data)
//        if (resultCode == RESULT_OK && data != null) {
//            if (data?.clipData !=null) {
//                val count = data?.clipData?.itemCount
//                val imageList: MutableList<String> = mutableListOf()
//                for (index in 0 until count!!) {
//                    val imageUri = data.clipData!!.getItemAt(index).uri
//                    Log.d("TAG", "${imageUri.toString()}")
//                    imageList.add(imageUri.toString())
//                }
//                adapter.addImagesUriList(imageList, position)
////                adapter.addImageUri(imageList[0], position)
//            }
//        }
//    }
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
                        Log.d("TAG","${imageUri.toString()}")
                        imageList.add(imageUri.toString())
                    }
                    adapter.addImagesUriList(imageList,position)
                    savePhotoViewModel.savePhotoDataList.value?.get(position)?.imageUriList = imageList
//                adapter.addImageUri(imageList[0], position)
                }
            }
        }
    }

    private fun initRouteFinishButton() {
        binding.btnRouteFinish.setOnClickListener {
            val frag = RecordBottomSheetDialog()
            frag.show(supportFragmentManager, frag.tag)
//            RecordPrefUtil.addRecord(Record(savePhotoDataList))
//            ContentIdPrefUtil.resetContentIdListPref()
//            finish()
        }
    }
}