package com.twoday.todaytrip.ui.save_photo

import android.content.Intent
import android.os.Bundle
import android.os.Parcelable
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.sangcomz.fishbun.FishBun
import com.sangcomz.fishbun.FishBun.Companion.INTENT_PATH
import com.sangcomz.fishbun.adapter.image.impl.GlideAdapter
import com.twoday.todaytrip.databinding.ActivitySavePhotoBinding
import com.twoday.todaytrip.ui.route.RecordBottomSheetDialog
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil


class SavePhotoActivity : AppCompatActivity() {
    private val TAG = "SavePhotoActivity"

    private val binding: ActivitySavePhotoBinding by lazy {
        ActivitySavePhotoBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: SavePhotoAdapter
    val savePhotoDataList = mutableListOf<SavePhotoData>()
    private var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initSavePhotoDataList()
        initSavePhotoRecylerView()
        initRouteFinishButton()
    }

    private fun initSavePhotoDataList() {
        val allTourItemList = TourItemPrefUtil.loadAllTourItemList()
        val addedContentIdList = ContentIdPrefUtil.loadContentIdList()

        addedContentIdList.forEach { contentID ->
            val tourItem = allTourItemList.find {
                it.getContentId() == contentID
            }!!
            savePhotoDataList.add(SavePhotoData(tourItem))
            if (allTourItemList.isNotEmpty()) {
//                binding.layoutRouteEmptyFrame.visibility = View.INVISIBLE
            }
        }
    }

    private fun initSavePhotoRecylerView() {
        adapter = SavePhotoAdapter(savePhotoDataList)
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

//    private fun initToolTip() {
//        //tooltip 버튼
//        binding.ivTooltip.setOnClickListener {
//            val balloon = createBalloon(context = this) {
//                setWidthRatio(1.0f)
//                setHeight(BalloonSizeSpec.WRAP)
//                setText("기록이 저정되며 해당탭이 초기화 됩니다\n저장한 기록은 기록 탭에서 보실 수 있습니다")
//                setTextColorResource(R.color.black)
//                setTextSize(15f)
//                setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
//                setArrowSize(10)
//                setArrowPosition(0.5f)
//                setPadding(12)
//                setMarginLeft(20)
//                setMarginRight(20)
//                setCornerRadius(8f)
//                elevation
//                setBackgroundColorResource(R.color.white)
//                setBalloonAnimation(BalloonAnimation.ELASTIC)
//                build()
//            }
//            balloon.showAlignBottom(binding.ivTooltip)
//            Handler(Looper.getMainLooper()).postDelayed({
//                balloon.dismiss()
//            }, 3000)
//        }
//    }

//    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
//        ActivityResultContracts.StartActivityForResult()
//    ) {
////        if (it.resultCode == RESULT_OK && it.data != null) {
//        if (it.resultCode == RESULT_OK) {
//
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
                        Log.d("sdc","${imageUri.toString()}")
                        imageList.add(imageUri.toString())
                    }
                    adapter.addImagesUriList(imageList,position)
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