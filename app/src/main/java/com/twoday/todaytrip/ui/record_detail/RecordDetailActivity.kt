package com.twoday.todaytrip.ui.record_detail

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.commit
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.ActivityRecordDetailBinding
import com.twoday.todaytrip.viewModel.RecordDetailViewModel
import com.twoday.todaytrip.ui.record.Record

class RecordDetailActivity : AppCompatActivity() {
    private val TAG = "RecordDetailActivity"

    private var _binding: ActivityRecordDetailBinding? = null
    private val binding get() = _binding!!

    private val model by lazy{
        ViewModelProvider(this@RecordDetailActivity)[RecordDetailViewModel::class.java]
    }
    private val record:Record? by lazy{
        intent.getParcelableExtra(EXTRA_RECORD)
    }

    private val onBackPressedCallback = object:OnBackPressedCallback(true){
        override fun handleOnBackPressed() {
            Log.d(TAG, "handleOnBackPressed) called")
            finish()
        }
    }

    companion object{
        private const val EXTRA_RECORD = "extra_record"
        fun newIntent(context: Context, record: Record): Intent =
            Intent(context, RecordDetailActivity::class.java).apply{
                putExtra(EXTRA_RECORD, record)
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityRecordDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        this.onBackPressedDispatcher.addCallback(this, onBackPressedCallback)
        initModelObserver()

        initUI()
        initBackButton()
        initDeleteButton()
        initOptionButton()
    }

    private fun initModelObserver(){
        model.isOptionMap.observe(this, Observer {
            setOptionIconAsMap(it)
        })
    }
    private fun setOptionIconAsMap(isOptionMap:Boolean){
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
    private fun initUI(){
        with(binding) {
            tvRecordDetailTitle.text = record?.destination
            tvRecordDetailDate.text = record?.travelDate
        }
    }
    private fun initBackButton(){
        binding.ivRecordDetailBack.setOnClickListener {
            finish()
        }

    }
    private fun initDeleteButton(){
        // TODO
    }
    private fun initOptionButton(){
        binding.ivRecordDetailOption.setOnClickListener {
            model.toggleIsOptionMap()
        }
    }
}