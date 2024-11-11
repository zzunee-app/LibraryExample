package com.zzunee.shoppingexample.common.ui

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class GridItemDecoration(
    private val columns: Int,
    private val offset: Int,
    private val includeEdge: Boolean = false,
) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State,
    ) {
        val position = parent.getChildAdapterPosition(view)
        val column = position % columns

        if (includeEdge) {
            outRect.left = offset

            if(column == columns - 1) { // 마지막 열에 우측 간격 추가
                outRect.right = offset
            }

            if (position < columns) { // 첫 번째 행
                outRect.top = offset
            }
            outRect.bottom = offset
        } else {
            if(column < columns - 1) { // 마지막 열 전까지만 우측 간격 추가
                outRect.right = offset
            }

            if (position >= columns) { // 첫번째 행이 아니면 위에 간격 추가
                outRect.top = offset
            }
        }
    }
}