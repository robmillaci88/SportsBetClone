package com.example.robmillaci.myapplication.extension_functions

import android.view.View
import android.view.ViewPropertyAnimator

fun View.pulse(){
    this.also{
        it.animate().apply {
            this.scaleX(1.5f)
            this.scaleY(1.5f)
            this.duration = 100
            this.withEndAction {
                it.animate().apply {
                    this.scaleX(1f)
                    this.scaleY(1f)
                    this.duration = 100
                } }
        }
    }
}

fun View.translationsSlide(offset : Float, applyAlpha : Float ){

        this.animate().apply {
            this.translationX(offset)
            this.duration = 500
            this.alpha(applyAlpha)
        }

}





