package com.example.robmillaci.myapplication.miscs

import android.content.Context
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class CustomLinearLayoutManager : LinearLayoutManager {
    constructor(context : Context?,  orientation : Int, reverseLayout : Boolean) : super(context, orientation , reverseLayout)
    constructor(context : Context) : super(context)

    override fun supportsPredictiveItemAnimations(): Boolean {
        return true
    }
}