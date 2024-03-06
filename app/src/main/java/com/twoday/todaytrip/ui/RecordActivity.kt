package com.twoday.todaytrip.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import com.db.williamchart.data.AxisType
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityRecordBinding
import com.twoday.todaytrip.utils.RecordPrefUtil

class RecordActivity : AppCompatActivity() {
    private val TAG = "RecordActivity"

    private var _binding: ActivityRecordBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecordBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Shared Preference test code
        val recordList = RecordPrefUtil.loadRecordList()
        Log.d(TAG, "${recordList.size}")
        if(recordList.isNotEmpty())
            Log.d(TAG, "${recordList[0].destination}, ${recordList[0].travelDate}, ${recordList[0].savePhotoDataList.size}")

        //WilliamsChart example code
        binding.tvChartEmpty.isVisible = false
        binding.chartRecord.isVisible = true

        val exampleDataSet = listOf<Pair<String, Float>>(
            "부산" to 10F,
            "영월" to 6F,
            "강원도 외 3" to 1F)
            .sortedBy { it.second }
        binding.chartRecord.run{
            axis = AxisType.Y
            labelsSize = 30F
            labelsColor = resources.getColor(R.color.middle_gray)

            animation.duration = 1800
            animate(exampleDataSet)
        }
    }
}