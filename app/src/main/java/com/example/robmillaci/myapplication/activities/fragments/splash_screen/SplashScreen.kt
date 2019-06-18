package com.example.robmillaci.myapplication.activities.fragments.splash_screen

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.activities.fragments.home_activity.HomeActivityView

/**
 * Splash screen upon launching the app. Shows to users for SPLASH_TIME
 * Before launching HomeActivity
 */
class SplashScreen : AppCompatActivity() {
    private val mHideUI = Runnable {
        //Hide the UI components to make this full screen
        window.decorView.systemUiVisibility =
            View.SYSTEM_UI_FLAG_LOW_PROFILE or
                    View.SYSTEM_UI_FLAG_FULLSCREEN or
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE or
                    View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY or
                    View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION or
                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    }

    //close the splash screen and launch Home activity after a specific delay
    private val closeSplashScreen = Handler().postDelayed({
        startActivity(Intent(this, HomeActivityView::class.java))
        this.finish()
    }, SPLASH_TIME)


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_splash_screen)
        mHideUI.run()
        closeSplashScreen
    }


    companion object {
        //For how long to display the splash screen
        private const val SPLASH_TIME = 1000L
    }
}
