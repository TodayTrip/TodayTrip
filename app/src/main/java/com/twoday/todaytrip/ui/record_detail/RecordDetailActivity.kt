package com.twoday.todaytrip.ui.record_detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.activity.viewModels
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityRecordDetailBinding
import com.twoday.todaytrip.viewModel.RecordDetailViewModel
import com.twoday.todaytrip.ui.record.Record
import com.twoday.todaytrip.ui.record_gallery.RecordGalleryFragment
import com.twoday.todaytrip.utils.RecordPrefUtil
import java.util.ArrayList

class RecordDetailActivity : AppCompatActivity(), DeleteRecordDialog.OnPositiveClickListener {
    private val TAG = "RecordDetailActivity"

    private var _binding: ActivityRecordDetailBinding? = null
    private val binding get() = _binding!!

    private val viewModel: RecordDetailViewModel by viewModels()

    private val record: Record? by lazy {
        intent.getParcelableExtra(EXTRA_RECORD)
    }

    private val onBackPressedCallback = object : OnBackPressedCallback(true) {
        override fun handleOnBackPressed() {
            Log.d(TAG, "handleOnBackPressed) called")
            finish()
        }
    }

    companion object {
        private const val EXTRA_RECORD = "extra_record"
        fun newIntent(context: Context, record: Record): Intent =
            Intent(context, RecordDetailActivity::class.java).apply {
                putExtra(EXTRA_RECORD, record)
                Log.d("TAG0 record의 savePhotoDataList", "${record?.savePhotoDataList?.size}")
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecordDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        initModelObserver()
        Log.d("TAG1 savePhotoDataList1", "${record?.savePhotoDataList?.size}")

        initUI()
        initBackButton()
        initDeleteButton()
        initOptionButton()
    }

    private fun initModelObserver() {
        viewModel.isOptionMap.observe(this, Observer {
            setOptionIconAsMap(it)
        })
    }

    private fun setOptionIconAsMap(isOptionMap: Boolean) {
        if (isOptionMap) {
            binding.ivRecordDetailOption.setImageResource(R.drawable.ic_record_detail_list)
            setFragment(RecordDetailListFragment())

        } else {
            binding.ivRecordDetailOption.setImageResource(R.drawable.ic_record_detail_gallery)
            setFragment(RecordGalleryFragment())
        }
    }

    private fun setFragment(fragment: Fragment) {
        when (fragment) {
            is RecordDetailListFragment -> {
                viewModel.setMarkersFromSavePhotoDataList(record?.savePhotoDataList)
                supportFragmentManager.commit {
                    replace(R.id.container_record_detail, fragment)
                    setReorderingAllowed(true)
                    addToBackStack("")
                }
            }

            else -> {
                viewModel.savePhotoDataUriList(record?.savePhotoDataList)
                supportFragmentManager.commit {
                    replace(R.id.container_record_detail, fragment)
                    setReorderingAllowed(true)
                    addToBackStack("")
                }
            }
        }
    }

    private fun initUI() {
        with(binding) {
            tvRecordDetailTitle.text = record?.destination
            tvRecordDetailDate.text = record?.travelDate
        }
    }

    private fun initBackButton() {
        binding.ivRecordDetailBack.setOnClickListener {
            finish()
        }

    }

    override fun onPositiveClick() {
        Log.d(TAG, "onPositiveClick) called")
        RecordPrefUtil.deleteRecord(record!!)
        finish()
    }

    private fun initDeleteButton() {
        binding.tvRecordDetailDelete.setOnClickListener {
            Log.d(TAG, "delete button clicked")
            val dialog = DeleteRecordDialog(this@RecordDetailActivity).apply {
                onPositiveClickListener = this@RecordDetailActivity
            }
            dialog.show()
        }
    }

    private fun initOptionButton() {
        binding.ivRecordDetailOption.setOnClickListener {
            viewModel.toggleIsOptionMap()
        }
    }
}