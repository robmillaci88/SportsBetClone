package com.example.robmillaci.myapplication.miscs

import android.os.Handler
import android.widget.TextView
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.adapters.ItimerCommunication
import com.example.robmillaci.myapplication.extension_functions.toHours
import com.example.robmillaci.myapplication.extension_functions.toMinutes
import com.example.robmillaci.myapplication.extension_functions.toSeconds

class Timer(val callback: ItimerCommunication) {


        fun createTimer(holderTextView: TextView?, holderPos : Int ,millisToGo: Long) {
            val seconds = millisToGo.toSeconds()
            val minutes = millisToGo.toMinutes()
            val hours = millisToGo.toHours()

            when {
                hours in 1..23 -> holderTextView?.text = String.format("%02dh %02dm", hours, minutes)

                minutes > 10 -> holderTextView?.text = String.format("%02dm %02ds", minutes, seconds)

                minutes > 0 -> {
                    holderTextView?.setBackgroundResource(R.drawable.timer_background_red)
                    holderTextView?.text = String.format("%02dm %02ds", minutes, seconds)
                }
                else -> {
                    holderTextView?.setBackgroundResource(R.drawable.timer_background_red)
                    holderTextView?.text = String.format("%02ds", seconds)
                }

            }

            if (millisToGo < 30000) {
                callback.removeItemOverTime(holderPos)
            } else {
                if (millisToGo != 0L) {
                    Handler().postDelayed({
                        createTimer(holderTextView, holderPos,millisToGo - 1000)
                    }, 1000)
                }
            }
        }
    }
