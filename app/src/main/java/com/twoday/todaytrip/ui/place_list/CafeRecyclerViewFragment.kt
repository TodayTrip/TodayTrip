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
import com.twoday.todaytrip.databinding.FragmentPlaceListCafeRecyclerViewBinding
import com.twoday.todaytrip.place_list_adapter.CafeRecyclerViewAdapter
import com.twoday.todaytrip.viewModel.MainViewModel


class CafeRecyclerViewFragment : Fragment(){
    private val TAG = "CafeRecyclerViewFragment"

    private var _binding: FragmentPlaceListCafeRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter: CafeRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaceListCafeRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initNoResultOnClickListener()
        initModelObserver()
    }

    private fun initRecyclerView(){
        adapter = CafeRecyclerViewAdapter()
        binding.rvCafeRecyclerView.adapter = adapter
    }

    private fun initNoResultOnClickListener(){
        binding.layoutCafeRecyclerViewNoResult.setOnClickListener {
            setLoadingUI(true)
            mainModel.fetchAndSaveCafeList()
        }
    }
    private fun initModelObserver(){
        mainModel.cafeTabList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.toMutableList())
            if(it.isEmpty())
                setNoResultUI()
            else
                setLoadingUI(false)
        })
    }
    private fun setNoResultUI(){
        binding.layoutCafeRecyclerViewNoResult.isVisible = true
        binding.layoutCafeRecyclerViewLoading.isVisible = false
    }
    private fun setLoadingUI(isLoading:Boolean){
        Log.d(TAG, "setLoadingUI) isLoading: $isLoading")
        binding.layoutCafeRecyclerViewNoResult.isVisible = false

        binding.layoutCafeRecyclerViewLoading.isVisible = isLoading
        binding.rvCafeRecyclerView.isVisible = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}