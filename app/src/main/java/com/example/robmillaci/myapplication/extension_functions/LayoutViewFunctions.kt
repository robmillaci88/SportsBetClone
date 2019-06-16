package com.example.robmillaci.myapplication.extension_functions

import android.content.Context
import android.graphics.drawable.Drawable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.pojos.SpecificTypes
import java.lang.ref.WeakReference

fun Int.inflate(context: Context?, parent: ViewGroup?): View {
    return LayoutInflater.from(context).inflate(this, parent, false)
}

fun ImageView.determineImageDrawable(weakContext: WeakReference<Context>, specificType: SpecificTypes): Drawable? {
    return when (specificType) {
        SpecificTypes.TENNIS -> weakContext.get()?.resources?.getDrawable(R.drawable.tennis_icon)
        SpecificTypes.CRICKET -> weakContext.get()?.resources?.getDrawable(R.drawable.cricket)
        SpecificTypes.RUGBY -> weakContext.get()?.resources?.getDrawable(R.drawable.nrl_icon)
        SpecificTypes.AFL -> weakContext.get()?.resources?.getDrawable(R.drawable.afl_icon)
        SpecificTypes.NRL -> weakContext.get()?.resources?.getDrawable(R.drawable.nrl_icon)
        SpecificTypes.SOCCER -> weakContext.get()?.resources?.getDrawable(R.drawable.soccer_icon)
        SpecificTypes.GREYHOUND -> weakContext.get()?.resources?.getDrawable(R.drawable.greys_icon)
        SpecificTypes.HORSES -> weakContext.get()?.resources?.getDrawable(R.drawable.horses_icon)
        SpecificTypes.HARNESS ->weakContext.get()?.resources?.getDrawable( R.drawable.harnes_icon)
    }

}