package com.example.robmillaci.myapplication.miscs

import android.os.Build
import android.util.Log
import android.view.View
import androidx.annotation.RequiresApi
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import android.view.animation.LinearInterpolator
import android.view.animation.Animation
import android.view.animation.RotateAnimation
import com.example.robmillaci.myapplication.R
import java.text.SimpleDateFormat
import java.util.*



@RequiresApi(Build.VERSION_CODES.O)
fun String.gimmeDateTime() : LocalDateTime{
    return LocalDateTime.parse(this, DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm"))
}

fun rotateTheView(v : View, containerView : View){
    val rotate : RotateAnimation?
    if (v.tag == "rotated"){
         rotate = RotateAnimation(180f, 360f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        v.tag = ""
        containerView.setBackgroundResource(android.R.color.white)
    }else {
         rotate = RotateAnimation(0f, 180f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f)
        v.tag = "rotated"
        containerView.setBackgroundResource(com.example.robmillaci.myapplication.R.color.very_light_grey)
    }

    rotate.duration = 300
    rotate.interpolator = LinearInterpolator()
    rotate.fillAfter=true
    v.startAnimation(rotate)

}

fun getCurrentDate(): String {
    val date = Date()
    val strDateFormat = "dd/MM/yyyy"
    val dateFormat = SimpleDateFormat(strDateFormat)
    Log.d("THEDATE", dateFormat.format(date))
    return dateFormat.format(date)
}




