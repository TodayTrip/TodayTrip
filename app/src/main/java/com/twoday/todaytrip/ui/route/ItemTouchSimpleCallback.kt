package com.twoday.todaytrip.ui.route

import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import java.util.Collections

class ItemTouchSimpleCallback : ItemTouchHelper.SimpleCallback(
    ItemTouchHelper.UP or ItemTouchHelper.DOWN, 0
) {

    interface OnItemMoveListener {
        fun onItemMove(from: Int, to: Int)
    }

    private var listener: OnItemMoveListener? = null

    fun setOnItemMoveListener(listener: OnItemMoveListener) {
        this.listener = listener
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ): Boolean {

        // 어댑터 획득
        val adapter = recyclerView.adapter as RouteAdapter

        // 현재 포지션 획득
        val fromPosition = viewHolder.absoluteAdapterPosition

        // 옮길 포지션 획득
        val toPosition = target.absoluteAdapterPosition

        // adapter 리스트를 담기위한 변수 생성
        val list = arrayListOf<RouteListData>()

        // adapter가 가지고 있는 현재 리스트 획득
        list.addAll(adapter.currentList)

        // 리스트 순서 바꿈
        Collections.swap(list, fromPosition, toPosition)

        // adapter.notifyItemMoved(fromPosition, toPosition)와 같은 역할
        // list를 adapter.differ.submitList()로 데이터 변경 사항 알림
        adapter.submitList(list)

        // 추가적인 조치가 필요할 경우 인터페이스를 통해 해결
        listener?.onItemMove(fromPosition, toPosition)

        return true
    }

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
    }

    // 드래그 완료 후 UI
    override fun clearView(recyclerView: RecyclerView, viewHolder: RecyclerView.ViewHolder) {
        super.clearView(recyclerView, viewHolder)

        // 순서 조정 완료 후 투명도 다시 1f로 변경
        viewHolder.itemView.alpha = 1.0f
    }

    // 드래그 중 UI 변화
    override fun onSelectedChanged(viewHolder: RecyclerView.ViewHolder?, actionState: Int) {

        if (actionState == ItemTouchHelper.ACTION_STATE_DRAG) {
            // 순서 변경 시 alpha를 0.5f
            viewHolder?.itemView?.alpha = 0.5f
        }
        super.onSelectedChanged(viewHolder, actionState)
    }
}