package com.example.robmillaci.myapplication.miscs

import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView
enum class CallingType{
    SPORT_CALLING_TYPE,
    BET_CALLING_TYPE
}

class SpacesItemDecoration(private val space: Int,val callType : CallingType) : RecyclerView.ItemDecoration() {

    override fun getItemOffsets(outRect: Rect, view: View, parent: RecyclerView, state: RecyclerView.State) {
        when(callType){
            CallingType.SPORT_CALLING_TYPE -> {
                outRect.left = space
                outRect.right = space
                outRect.bottom = space
            }

            CallingType.BET_CALLING_TYPE -> {
                outRect.bottom = space
            }
        }

    }
}