package com.twoday.todaytrip.ui.random

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CompoundButton
import com.devs.vectorchildfinder.VectorChildFinder
import com.devs.vectorchildfinder.VectorDrawableCompat
import com.google.android.material.chip.Chip
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentSelectRegionBinding

class SelectRegionFragment : Fragment() {

    private val TAG = "SelectRegionBinding"

    companion object {
        fun newInstance() = SelectRegionFragment()
    }

    private var _binding: FragmentSelectRegionBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: SelectRegionViewModel

    private lateinit var regionList: List<VectorDrawableCompat.VFullPath>

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSelectRegionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProvider(this).get(SelectRegionViewModel::class.java)
        initView()
    }

    private fun initView() {
        fillMap()
    }

    private fun fillMap() {
        val map = binding.ivSelectRegionMap
        val mapVector = VectorChildFinder(context, R.drawable.img_select_region, map)

        var selectedChips = listOf(
            binding.chipSelectRegionSeoul,
            binding.chipSelectRegionIncheon,
            binding.chipSelectRegionJeonbuk,
            binding.chipSelectRegionJeonnam,
            binding.chipSelectRegionGyeongbuk,
            binding.chipSelectRegionGyeongnam,
            binding.chipSelectRegionChungbuk,
            binding.chipSelectRegionChungnam,
            binding.chipSelectRegionGangwon,
            binding.chipSelectRegionDaegu,
            binding.chipSelectRegionBusan,
            binding.chipSelectRegionDaejeon,
            binding.chipSelectRegionJeju,
            binding.chipSelectRegionGyeonggi,
            binding.chipSelectRegionGwangju,
            binding.chipSelectRegionUlsan
        )

        selectedChips.forEach {
            val selected = mapVector.findPathByName(it.text.toString())
            it.setOnCheckedChangeListener { buttonView, isChecked ->
                if (isChecked) {
                    selected.fillColor = Color.BLUE
                } else {
                    selected.fillColor = Color.GRAY
                }
                map.invalidate()
            }
            Log.d(TAG, "selected chip=${it.text.toString()}, selected map=${selected.toString()}")
        }



    }

    //선택한 지역 shared preference에 저장
    //선택한 지역 내에서만 랜덤
    //하나이상선택시 다음버튼 활성화


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}