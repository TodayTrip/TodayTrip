package com.twoday.todaytrip.ui.route

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.ItemTouchHelper
import com.naver.maps.map.LocationTrackingMode
import com.naver.maps.map.NaverMap
import com.naver.maps.map.OnMapReadyCallback
import com.naver.maps.map.util.FusedLocationSource
import com.twoday.todaytrip.R
import com.twoday.todaytrip.databinding.FragmentRouteBinding
import com.twoday.todaytrip.utils.ContentIdPrefUtil
import com.twoday.todaytrip.utils.TourItemPrefUtil


class RouteFragment : Fragment(), OnMapReadyCallback {

    //    private lateinit var adapter: RouteAdapter
    private val itemTouchSimpleCallback = ItemTouchSimpleCallback()
    private val itemTouchHelper = ItemTouchHelper(itemTouchSimpleCallback)
    private val adapter: RouteAdapter by lazy {
        RouteAdapter()
    }
    private lateinit var binding: FragmentRouteBinding
//    private lateinit var mContext: Context

//    private val itemTouchHelper by lazy { ItemTouchHelper(ItemTouchCallback(RouteAdapter)) }


    private lateinit var map: NaverMap
    private lateinit var locationSource: FusedLocationSource


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        binding = FragmentRouteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val contentIdList = ContentIdPrefUtil.loadContentIdList()
        val tour = TourItemPrefUtil.loadTouristAttractionList()
        val rest = TourItemPrefUtil.loadRestaurantList()
        val cafe = TourItemPrefUtil.loadCafeList()
        val event = TourItemPrefUtil.loadEventList()
        val tourLIst = tour + rest + cafe + event
        val dataSet:MutableList<RouteListData> = mutableListOf()

        //순서 변경하면 변경사항 저장되게하기
        //담은 아이템 RecyclerView에 띄우기
        if(contentIdList.isNotEmpty()) {
            var count = 0
            contentIdList.forEach {
                val place = tourLIst.find { it.getContentId() == contentIdList[count] }
                Log.d("sdc","최초카운트 = $count")
                dataSet.add(RouteListData(place?.getTitle() ?: "", place?.getAddress() ?: ""))
                if (contentIdList.size-1 > count) {
                    count += 1
                }
            }
        }

        adapter.submitList(dataSet)
        binding.rvRouteRecyclerview.adapter = adapter
        //리싸이클러뷰에 아무것도 없을시 아이콘 띄움
        if (dataSet.isNotEmpty()){
            binding.layoutRouteEmptyFrame.visibility = View.INVISIBLE
        }

        binding.btnRouteFinish.setOnClickListener {
            val frag = BottomSheetDialog()
            frag.show(childFragmentManager, frag.tag)
        }

        // itemTouchSimpleCallback 인터페이스로 추가 작업
        itemTouchSimpleCallback.setOnItemMoveListener(object :
            ItemTouchSimpleCallback.OnItemMoveListener {
            override fun onItemMove(from: Int, to: Int) {
//                adapter.notifyItemMoved(from,to)
//                adapter.submitList(datalist)
                binding.rvRouteRecyclerview.adapter = adapter

            }
        })

        // itemTouchHelper와 recyclerview 연결, 아이템 순서변경
        itemTouchHelper.attachToRecyclerView(binding.rvRouteRecyclerview)

//        binding.cvRouteEditFrame.setOnClickListener {
//            if (binding.tvRouteListEdit.isVisible) {
//                binding.tvRouteListEdit.visibility = View.INVISIBLE
//                binding.tvRouteListCompletion.visibility = View.VISIBLE
//            } else {
//                binding.tvRouteListEdit.visibility = View.VISIBLE
//                binding.tvRouteListCompletion.visibility = View.INVISIBLE
//            }
//            Toast.makeText(context, "편집클릭", Toast.LENGTH_SHORT).show()
//        }

        adapter.itemClick = object : RouteAdapter.ItemClick {
            override fun onClick(item: RouteListData) {

//                (activity as? MainActivity)?.removeFavorites(item)
//                adapter.notifyDataSetChanged()

                Log.d("favoritefragment", "remove  ${item}")

            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RouteFragment().apply {
                arguments = Bundle().apply {
//                    putString(ARG_PARAM1, param1)
//                    putString(ARG_PARAM2, param2)
                }
            }
    }

    override fun onMapReady(naverMap: NaverMap) {
        this.map = naverMap
        naverMap.locationSource = locationSource
        naverMap.uiSettings.isLocationButtonEnabled = true
        naverMap.locationTrackingMode = LocationTrackingMode.Face
    }

//    private fun bindModify() {
//        findViewById(R.id.btn_modify).setOnClickListener(object : OnClickListener() {
//            fun onClick(v: View?) {
//                val recyclerItem: PhRecyclerItem = mRecyclerAdapter.getSelected()
//                if (recyclerItem == null) {
//                    Toast.makeText(
//                        this@PhMainActivity,
//                        R.string.err_no_selected_item,
//                        Toast.LENGTH_SHORT
//                    ).show()
//                    return
//                }
//
//                // Recycler item 수정
//                recyclerItem.setName(recyclerItem.getName() + " is modified")
//
//                // List 반영
//                // mRecyclerAdapter.notifyDataSetChanged();
//                val checkedPosition: Int = mRecyclerAdapter.getCheckedPosition()
//                mRecyclerAdapter.notifyItemChanged(checkedPosition)
//
//                // 선택 항목 초기화
//                mRecyclerAdapter.clearSelected()
//            }
//        })
//    }
}