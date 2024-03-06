package com.twoday.todaytrip.ui.save_photo

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.twoday.todaytrip.databinding.ActivitySavePhotoBinding
import com.twoday.todaytrip.ui.record.Record
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.RecordPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil


class SavePhotoActivity : AppCompatActivity() {
    private val TAG = "SavePhotoActivity"

    private val binding: ActivitySavePhotoBinding by lazy {
        ActivitySavePhotoBinding.inflate(layoutInflater)
    }

    private lateinit var adapter: SavePhotoAdapter
    private val savePhotoDataList = mutableListOf<SavePhotoData>()


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
                val intent = Intent(Intent.ACTION_PICK)
                intent.type = "image/*"
                activityResult.launch(intent)
                Toast.makeText(this@SavePhotoActivity, "클릭", Toast.LENGTH_SHORT).show()
            }
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

    private fun initRouteFinishButton(){
        binding.layoutRouteFinishButton.setOnClickListener {
            RecordPrefUtil.addRecord(Record(savePhotoDataList))
            ContentIdPrefUtil.resetContentIdListPref()
            finish()
        }
    }
}