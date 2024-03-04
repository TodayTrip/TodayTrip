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
import com.twoday.todaytrip.databinding.FragmentPlaceListRestaurantRecyclerViewBinding
import com.twoday.todaytrip.place_list_adapter.RestaurantRecyclerViewAdapter
import com.twoday.todaytrip.viewModel.MainViewModel


class RestaurantRecyclerViewFragment : Fragment(){
    private val TAG = "RestaurantRecyclerViewFragment"

    private var _binding: FragmentPlaceListRestaurantRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter: RestaurantRecyclerViewAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaceListRestaurantRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initRecyclerView()
        initNoResultOnClickListener()
        initModelObserver()
    }

    private fun initRecyclerView(){
        adapter = RestaurantRecyclerViewAdapter()
        binding.rvRestaurantRecyclerView.adapter = adapter
    }

    private fun initNoResultOnClickListener(){
        binding.layoutRestaurantRecyclerViewNoResult.setOnClickListener {
            setLoadingUI(true)
            mainModel.fetchAndSaveRestaurantList()
        }
    }
    private fun initModelObserver(){
        mainModel.restaurantList.observe(viewLifecycleOwner, Observer {
            adapter.submitList(it.toMutableList())
            if(it.isEmpty())
                setNoResultUI()
            else
                setLoadingUI(false)
        })
    }
    private fun setNoResultUI(){
        binding.layoutRestaurantRecyclerViewNoResult.isVisible = true
        binding.layoutRestaurantRecyclerViewLoading.isVisible = false
    }
    private fun setLoadingUI(isLoading:Boolean){
        Log.d(TAG, "setLoadingUI) isLoading: $isLoading")
        binding.layoutRestaurantRecyclerViewNoResult.isVisible = false

        binding.layoutRestaurantRecyclerViewLoading.isVisible = isLoading
        binding.rvRestaurantRecyclerView.isVisible = !isLoading
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}