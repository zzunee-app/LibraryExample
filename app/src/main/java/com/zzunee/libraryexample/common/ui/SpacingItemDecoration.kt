package com.zzunee.libraryexample.common.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class SpacingItemDecoration(
    private val offset: Int,  // 양 끝의 패딩
    private val isHorizontal: Boolean = false,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val itemCount = state.itemCount

        if (isHorizontal) {
            setHorizontalPadding(position, itemCount, outRect)
        } else {
            setVerticalPadding(position, itemCount, outRect)
        }
    }

    // 좌우 패딩 추가
    private fun setHorizontalPadding(position: Int, itemCount: Int, outRect: Rect) {
        when (position) {
            0 -> { // 첫번째
                outRect.left = offset
                outRect.right = offset / 2
            }

            itemCount - 1 -> {  // 마지막
                outRect.left = offset / 2
                outRect.right = offset
            }

            else -> {  // 중간 아이템
                outRect.left = offset / 2
                outRect.right = offset / 2
            }
        }
    }

    // 상하 패딩 추가
    private fun setVerticalPadding(position: Int, itemCount: Int, outRect: Rect) {
        when (position) {
            0 -> { // 첫번째
                outRect.top = offset
                outRect.bottom = offset / 2
            }

            itemCount - 1 -> {  // 마지막
                outRect.top = offset / 2
                outRect.bottom = offset
            }

            else -> {  // 중간 아이템
                outRect.top = offset / 2
                outRect.bottom = offset / 2
            }
        }
    }
}
