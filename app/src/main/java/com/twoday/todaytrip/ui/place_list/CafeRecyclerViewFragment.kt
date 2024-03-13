package com.twoday.todaytrip.ui.place_list

import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.twoday.todaytrip.databinding.FragmentPlaceListCafeRecyclerViewBinding
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemClickListener
import com.twoday.todaytrip.ui.place_list.adapter.PlaceListAdapter
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.viewModel.MainViewModel


class CafeRecyclerViewFragment : Fragment(), OnTourItemClickListener {
    private val TAG = "CafeRecyclerViewFragment"

    private var _binding: FragmentPlaceListCafeRecyclerViewBinding? = null
    private val binding get() = _binding!!

    private val mainModel: MainViewModel by activityViewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T =
                MainViewModel() as T
        }
    }

    private lateinit var adapter: PlaceListAdapter
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaceListCafeRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        setLoadingUI(true)
        initRecyclerView()
        initModelObserver()
    }
    private fun setLoadingUI(isLoading: Boolean) {
        binding.shimmerCafeRecyclerView.isVisible = isLoading
        binding.rvCafeRecyclerView.isVisible = !isLoading

        if(isLoading) binding.shimmerCafeRecyclerView.startShimmer()
    }
    private fun initRecyclerView(){
        adapter = PlaceListAdapter().apply{
            onTourItemClickListener = this@CafeRecyclerViewFragment
        }
        binding.rvCafeRecyclerView.adapter = adapter
    }
    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onTourItemClick(tourItem: TourItem) {
        Log.d(TAG, "onTourItemClick) called, ${tourItem.getTitle()}")
        val placeDetailIntent = PlaceDetailActivity.newIntent(
            requireContext(),
            tourItem.getContentTypeId(),
            tourItem)
        startActivity(placeDetailIntent)
    }

    private fun initModelObserver(){
        mainModel.cafeList.observe(viewLifecycleOwner, Observer {
            Log.d(TAG, "cafe list size: ${it.size}")
            adapter.submitList(it.toMutableList())
            setLoadingUI(false)
        })
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}