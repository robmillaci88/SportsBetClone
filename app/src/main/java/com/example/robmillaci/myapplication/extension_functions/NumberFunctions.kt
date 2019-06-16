package com.example.robmillaci.myapplication.extension_functions

import java.util.concurrent.TimeUnit

fun Long.toSeconds() : Int {
   return (TimeUnit.MILLISECONDS.toSeconds(this) - TimeUnit.MINUTES.toSeconds(
        TimeUnit.MILLISECONDS.toMinutes(this)
    )).toInt()
}

fun Long.toMinutes() : Int {
   return (TimeUnit.MILLISECONDS.toMinutes(this) - TimeUnit.HOURS.toMinutes(
        TimeUnit.MILLISECONDS.toHours(this)
    )).toInt()
}

fun Long.toHours() : Int {
    return  TimeUnit.MILLISECONDS.toHours(this).toInt()
}


