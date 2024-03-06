package com.twoday.todaytrip.ui.route

import android.Manifest.permission.READ_EXTERNAL_STORAGE
import android.R
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.TooltipCompat
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import coil.imageLoader
import coil.load
import com.bumptech.glide.Glide
import com.skydoves.balloon.ArrowPositionRules
import com.skydoves.balloon.BalloonAnimation
import com.skydoves.balloon.BalloonSizeSpec
import com.skydoves.balloon.createBalloon
import com.twoday.todaytrip.MyApplication
import com.twoday.todaytrip.databinding.ActivitySavePhotoBinding
import com.twoday.todaytrip.ui.RecordActivity
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil
import okhttp3.internal.notifyAll


class SavePhotoActivity : AppCompatActivity() {
    private val TAG = "SavePhotoActivity"

    private val binding: ActivitySavePhotoBinding by lazy {
        ActivitySavePhotoBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: SavePhotoAdapter
    private val savePhotoDataList = mutableListOf<SavePhotoData>()
    private var position = 0


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        initSavePhotoDataList()
        initSavePhotoRecylerView()
        initRouteFinishButton()
    }

    private fun initSavePhotoDataList(){
        val allTourItemList = TourItemPrefUtil.loadAllTourItemList()
        val addedContentIdList = ContentIdPrefUtil.loadContentIdList()

        addedContentIdList.forEach {contentID ->
            val tourItem = allTourItemList.find{
                it.getContentId() == contentID
            }!!
            savePhotoDataList.add(SavePhotoData(tourItem))
        }
    }

    private fun initSavePhotoRecylerView(){
        adapter = SavePhotoAdapter(savePhotoDataList)
        binding.rvSavephotoRecyclerview.adapter = adapter

        adapter.itemClick = object : SavePhotoAdapter.ItemClick {
            override fun onClick(item: SavePhotoData, position: Int) {
                this@SavePhotoActivity.position = position
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



        //tooltip 버튼
        binding.ivTooltip.setOnClickListener {
            val balloon = createBalloon(context = this) {
                setWidthRatio(1.0f)
                setHeight(BalloonSizeSpec.WRAP)
                setText("기록이 저정되며 해당탭이 초기화 됩니다\n저장한 기록은 기록 탭에서 보실 수 있습니다")
                setTextColorResource(R.color.black)
                setTextSize(15f)
                setArrowPositionRules(ArrowPositionRules.ALIGN_ANCHOR)
                setArrowSize(10)
                setArrowPosition(0.5f)
                setPadding(12)
                setMarginLeft(20)
                setMarginRight(20)
                setCornerRadius(8f)
                elevation
                setBackgroundColorResource(R.color.white)
                setBalloonAnimation(BalloonAnimation.ELASTIC)
                build()
            }
            balloon.showAlignBottom(binding.ivTooltip)
            Handler(Looper.getMainLooper()).postDelayed({
                balloon.dismiss()
            }, 3000)
        }
    }
    private val activityResult: ActivityResultLauncher<Intent> = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) {
        if (it.resultCode == RESULT_OK && it.data != null) {
            val uri = it.data!!.data!!
            Glide.with(this).load(uri).into(binding.imgLoad)
            adapter.addImageUri(uri, position)
        }
    }

    private fun initRouteFinishButton(){
        binding.layoutRouteFinishButton.setOnClickListener {
            RecordPrefUtil.addRecord(Record(savePhotoDataList))
            ContentIdPrefUtil.resetContentIdListPref()
            finish()
        }
    }
}