package com.twoday.todaytrip.ui.record

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.db.williamchart.data.AxisType
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRecordBinding
import com.twoday.todaytrip.utils.DestinationData
import com.twoday.todaytrip.viewModel.RecordViewModel

class RecordFragment : Fragment() {
    private val TAG = "RecordFragment"

    private var _binding: FragmentRecordBinding? = null
    private val binding get() = _binding!!

    private val model by lazy {
        ViewModelProvider(this@RecordFragment)[RecordViewModel::class.java]
    }

    private lateinit var recordAdapter: RecordAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentRecordBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initModelObserver()
        initRecordRecyclerView()
    }

    override fun onResume() {
        super.onResume()
        model.loadRecordList()
    }
    private fun initModelObserver() {
        model.recordList.observe(viewLifecycleOwner, Observer {
            initTitleLayout(it)

            val chartDataList =  model.getChartDataList()
            if (chartDataList.isEmpty()) {
                setChartVisibility(false)
            }
            else{
                setChartVisibility(true)
                setChartData(chartDataList)
            }

            if(it.isEmpty()){
                setRecordRecyclerViewVisibility(false)
            }
            else{
                setRecordRecyclerViewVisibility(true)
                recordAdapter.submitList(it.toMutableList())
            }
        })
    }

    private fun initTitleLayout(recordList: List<Record>){
        binding.tvRecordSubTitle.text = getString(R.string.record_sub_title, recordList.size)
    }

    private fun setChartData(chartDataList:List<Pair<String, Float>>) {
        binding.chartRecord.run {
            axis = AxisType.Y
            labelsSize = 30F
            labelsColor = resources.getColor(R.color.middle_gray)
            animation.duration = 1800
            animate(chartDataList)
        }
    }
    private fun setChartVisibility(isVisible: Boolean) {
        binding.chartRecord.isVisible = isVisible
        binding.tvChartEmpty.isVisible = !isVisible
    }

    private fun initRecordRecyclerView(){
        recordAdapter = RecordAdapter()
        binding.rvRecord.adapter = recordAdapter
    }
    private fun setRecordRecyclerViewVisibility(isVisible:Boolean){
        binding.rvRecord.isVisible = isVisible
        binding.layoutRecordEmpty.isVisible = !isVisible
    }
}