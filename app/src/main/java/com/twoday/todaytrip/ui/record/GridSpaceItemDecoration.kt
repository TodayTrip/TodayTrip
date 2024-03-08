package com.twoday.todaytrip.ui.record

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridSpaceItemDecoration(private val spanCount: Int, private val space: Int) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % spanCount + 1

        if (position >= spanCount) {
            outRect.top = space
        }
        outRect.bottom = space

        if (column != 1) {
            outRect.left = space
        }
    }
}