package com.github.amusinamaria.ui.list

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MoviesRVSpacingDecoration(private val columnsCount: Int, private val spacing: Int = 34) :
    RecyclerView.ItemDecoration() {

    override fun getItemOffsets(
        outRect: Rect,
        view: View,
        parent: RecyclerView,
        state: RecyclerView.State
    ) {
        val position: Int = parent.getChildAdapterPosition(view)
        val column = position % columnsCount

        outRect.left = spacing - column * spacing / columnsCount
        outRect.right = (column + 1) * spacing / columnsCount
        outRect.bottom = spacing
    }
}
