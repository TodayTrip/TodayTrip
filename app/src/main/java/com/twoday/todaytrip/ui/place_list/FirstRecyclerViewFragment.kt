package com.twoday.todaytrip.ui.place_list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.place_list_adapter.FirstRecyclerViewAdapter
import com.twoday.todaytrip.databinding.FragmentFirstRecyclerViewBinding
import com.twoday.todaytrip.viewModel.MainViewModel


class FirstRecyclerViewFragment : Fragment() {
    private val TAG = "FirstRecyclerViewFragment"

    private var _binding: FragmentFirstRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter:FirstRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentFirstRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initNoResultOnClickListener()
        initModelObserver()
    }

    private fun initRecyclerView(){
        adapter = FirstRecyclerViewAdapter()
        binding.rvFirstRecyclerView.adapter = adapter
    }
    private fun initNoResultOnClickListener(){
        binding.layoutFirstRecyclerViewNoResult.setOnClickListener {
            setLoadingUI(true)
            mainModel.fetchAndSaveTouristAttractionList()
        }
    }
    private fun initModelObserver(){
        mainModel.tourInfoTabList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it)
            if(it.isEmpty())
                setNoResultUI()
            else
                setLoadingUI(false)
        })
    }
    private fun setNoResultUI(){
        binding.layoutFirstRecyclerViewNoResult.isVisible = true
        binding.layoutFirstRecyclerViewLoading.isVisible = false
    }
    private fun setLoadingUI(isLoading:Boolean){
        Log.d(TAG, "setLoadingUI) isLoading: $isLoading")
        binding.layoutFirstRecyclerViewNoResult.isVisible = false

        binding.layoutFirstRecyclerViewLoading.isVisible = isLoading
        binding.rvFirstRecyclerView.isVisible = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}