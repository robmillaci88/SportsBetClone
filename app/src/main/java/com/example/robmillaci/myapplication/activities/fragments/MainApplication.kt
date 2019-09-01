package com.example.robmillaci.myapplication.activities.fragments

import android.app.Application

class MainApplication  : Application() {
    companion object{
        val sMultiRacingBets = ArrayList<String>().apply {
            this.add("hello")
        } //maintain users multi-bets across the entire application lifetime
    }
}