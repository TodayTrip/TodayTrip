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
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentPlaceListCafeRecyclerViewBinding
import com.twoday.todaytrip.ui.place_list.adapter.OnTourItemClickListener
import com.twoday.todaytrip.ui.place_list.adapter.PlaceListAdapter
import com.twoday.todaytrip.tourData.TourItem
import com.twoday.todaytrip.ui.place_detail.PlaceDetailActivity
import com.twoday.todaytrip.utils.showSnackBar
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

    private val cafeAdapter = PlaceListAdapter()
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        _binding = FragmentPlaceListCafeRecyclerViewBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initUI()
        initSwipeRefreshLayout()
        initRecyclerView()
        initModelObserver()
    }
    override fun onResume() {
        super.onResume()
        mainModel.loadOrFetchCafeList()
    }

    private fun initUI(){
        setLoadingUI(true)
        setNoResultUI(false)

        Glide.with(requireContext())
            .load(resources.getDrawable(R.drawable.gif_loading_reading_glasses))
            .into(binding.ivCafeRecyclerViewNoResult)
    }
    private fun setNoResultUI(isNoResult: Boolean){
        Log.d(TAG, "setNoResultUI) isNoResult: $isNoResult")
        binding.layoutCafeRecyclerViewNoResult.isVisible = isNoResult
    }
    private fun setLoadingUI(isLoading: Boolean) {
        Log.d(TAG, "setLoadingUI) isLoading: $isLoading")
        binding.shimmerCafeRecyclerView.isVisible = isLoading
        if (isLoading) binding.shimmerCafeRecyclerView.startShimmer()
    }

    private fun initSwipeRefreshLayout(){
        binding.swipeCafeRecyclerView.setOnRefreshListener {
            setNoResultUI(false)
            setLoadingUI(true)
            mainModel.loadOrFetchCafeList()

            binding.swipeCafeRecyclerView.isRefreshing = false
        }
    }
    private fun initRecyclerView(){
        cafeAdapter.onTourItemClickListener = this@CafeRecyclerViewFragment
        binding.rvCafeRecyclerView.run{
            this.adapter = cafeAdapter
            initScrollListener(this)
        }
    }
    private fun initScrollListener(recyclerView: RecyclerView){
        recyclerView.setOnScrollListener(object : RecyclerView.OnScrollListener(){
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)

                if (!recyclerView.canScrollVertically(1)) {
                    Log.d(TAG, "recyclerview end of scroll!")
                    Log.d(TAG, "adapter current list size: ${cafeAdapter.currentList.size}")
                    Log.d(TAG, "isCafeLoadReady: ${mainModel.isCafeLoadReady}")

                    if((cafeAdapter.currentList.isNotEmpty()) &&
                        (mainModel.isCafeLoadReady)){
                        Log.d(TAG, "fetch and save more cafe list")
                        cafeAdapter.addDummyTourItem()
                        mainModel.fetchAndSaveMoreCafeList()
                    }
                }
            }
        })
    }

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onTourItemClick(tourItem: TourItem) {
        Log.d(TAG, "onTourItemClick) called, ${tourItem.getTitle()}")
        val placeDetailIntent = PlaceDetailActivity.newIntent(
            requireContext(),
            tourItem.getContentTypeId(),
            tourItem)
        startActivity(placeDetailIntent)
        requireActivity().overridePendingTransition(R.anim.fade_in, R.anim.fade_out)
    }

    private fun initModelObserver(){
        mainModel.cafeList.observe(viewLifecycleOwner, Observer { cafeList ->
            Log.d(TAG, "observe) cafe list size: ${cafeList.size}")
            cafeAdapter.submitList(cafeList.toMutableList())
            Log.d(TAG, "observe) current list size: ${cafeAdapter.currentList.size}")

            setLoadingUI(false)
            if(cafeList.isEmpty()) setNoResultUI(true)
        })

        mainModel.cafeMoreLoaded.observe(viewLifecycleOwner){
            if(it == 0) return@observe

            Log.d(TAG, "observe) cafeMoreLoaded: $it")
            cafeAdapter.removeDummyTourItem()
            showSnackBar(
                message = R.string.place_list_more_cafe_no_result,
                anchorView = requireActivity().findViewById(R.id.fab_bottom_random)
            )
            mainModel.setCafeMoreLoadedDefault()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}