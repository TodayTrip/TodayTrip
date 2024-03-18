package com.twoday.todaytrip.ui.place_list.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager.widget.PagerAdapter
import com.twoday.todaytrip.R

class ViewPagerAdapter : PagerAdapter() {
    private var mContext: Context? = null

    fun ViewPagerAdapter(context: Context) {
        mContext = context;
    }

    //position에 해당하는 페이지 생성
    override fun instantiateItem(container: ViewGroup, position: Int): Any {
        val view = LayoutInflater.from(container.context)
            .inflate(R.layout.item_place_list_recommend, container, false)
        container.addView(view)
        return view
    }

    //position에 위치한 페이지 제거
    override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
        container.removeView(`object` as View?)
    }


    //사용가능한 뷰 개수 리턴
    override fun getCount(): Int {
        return 5
    }

    //페이지뷰가 특정 키 객체(key object)와 연관 되는지 여부
    override fun isViewFromObject(view: View, `object`: Any): Boolean {
        return (view == `object`)
    }
}