package com.twoday.todaytrip.ui.record

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import com.db.williamchart.data.AxisType
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRecordBinding
import com.twoday.todaytrip.utils.RecordPrefUtil

class RecordFragment : Fragment() {
    private val TAG = "RecordActivity"
    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    val recordList = RecordPrefUtil.loadRecordList()
//        Log.d(TAG, "${recordList.size}")
//        if(recordList.isNotEmpty())
//            Log.d(TAG, "${recordList[0].destination}, ${recordList[0].travelDate}, ${recordList[0].savePhotoDataList.size}")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
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