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
import okhttp3.internal.notifyAll


class SavePhotoActivity : AppCompatActivity() {

    private val binding: ActivitySavePhotoBinding by lazy {
        ActivitySavePhotoBinding.inflate(layoutInflater)
    }

        private lateinit var adapter: SavePhotoAdapter
//    private val adapter: SavePhotoAdapter by lazy {
//        SavePhotoAdapter()
//    }

    private val datalist = mutableListOf<SavePhotoData>()
    private val currentList: MutableList<SavePhotoData> = mutableListOf()
    private var uri = "".toUri()

//    private lateinit var uri: Uri





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
//                val uridata = currentList.get(0)
//                datalist[position] = SavePhotoData(uridata,"d","d")
                Toast.makeText(this@SavePhotoActivity, "클릭", Toast.LENGTH_SHORT).show()

                val place = datalist.find { it.position == position }
                currentList.add(SavePhotoData(uri,place?.name ?: "", place?.address ?: ""))
                val name = currentList.get(0).name
                val address = currentList.get(0).address
                datalist.set(position,SavePhotoData(uri,name,address))
//                Log.d("sdc","초기화 전 currentlist = ${currentList[position]}")
                currentList.clear()
//                Log.d("sdc","name = $name")
//                Log.d("sdc","address = $address")
//                Log.d("sdc","datalist = ${datalist[position]}")
//                Log.d("sdc","초기화 후 currentlist = ${currentList[position]}")




//                datalist[position].image = uri
//                adapter.notifyItemChanged(position)
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
        ActivityResultContracts.StartActivityForResult()){
        if(it.resultCode == RESULT_OK && it.data != null) {
//            uri = it.data!!.data!!
//            grantUriPermission(
//                "com.twoday.todaytrip",
//                uri,
//                Intent.FLAG_GRANT_READ_URI_PERMISSION
//            )
//            Glide.with(this).
//                    load(uri).into(binding.imgLoad)
            binding.imgLoad.load(uri)
//            uri?.let { it1 -> currentList.add(0, it1) }
        }
    }



}