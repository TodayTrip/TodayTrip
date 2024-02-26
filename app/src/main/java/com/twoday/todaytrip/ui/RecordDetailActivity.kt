package com.twoday.todaytrip.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityRecordDetailBinding
import com.twoday.todaytrip.viewmodel.RecordDetailViewModel

class RecordDetailActivity : AppCompatActivity() {
    private var _binding: ActivityRecordDetailBinding? = null
    private val binding get() = _binding!!

    private val model by lazy{
        ViewModelProvider(this@RecordDetailActivity)[RecordDetailViewModel::class.java]
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecordDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        initUI(true)

        initModelObserver()

        initOptionButton()
    }

    private fun initModelObserver(){
        model.isOptionMap.observe(this, Observer {
            initUI(it)
        })
    }
    private fun initUI(isOptionMap:Boolean){
        if(isOptionMap){
            binding.ivRecordDetailOption.setImageResource(R.drawable.ic_record_detail_list)
            setFragment(RecordDetailMapFragment())
        }
        else{
            binding.ivRecordDetailOption.setImageResource(R.drawable.ic_record_detail_map)
            setFragment(RecordDetailListFragment())
        }
    }
    private fun setFragment(fragment: Fragment){
        supportFragmentManager.commit {
            replace(R.id.container_record_detail, fragment)
            setReorderingAllowed(true)
            addToBackStack("")
        }
    }

    private fun initOptionButton(){
        binding.ivRecordDetailOption.setOnClickListener {
            model.toggleIsOptionMap()
        }
    }
}