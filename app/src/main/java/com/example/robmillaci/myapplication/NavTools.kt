package com.example.robmillaci.myapplication

import android.app.Activity
import android.content.Context
import android.view.MenuItem
import android.widget.ImageView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.drawerlayout.widget.DrawerLayout
import com.example.robmillaci.myapplication.activities.fragments.home_activity.BetSlipDrawerOpened
import com.google.android.material.navigation.NavigationView


open class NavTools(val drawerOpenedListener: BetSlipDrawerOpened) : NavigationView.OnNavigationItemSelectedListener {
    override fun onNavigationItemSelected(p0: MenuItem): Boolean {
        return true
    }

    fun configureBetSlipView(context: Context, nav_view: NavigationView?): NavigationView? {
        (context as Activity).findViewById<ImageView>(R.id.bet_slip).setOnClickListener {
            (context).findViewById<DrawerLayout>(R.id.drawer_layout).openDrawer(nav_view!!)
            nav_view.setNavigationItemSelectedListener(this)
            drawerOpenedListener.onOpenBetSlip()
        }
        return nav_view
    }


    fun configureNavView(
        context: Context,
        toolbarMain: androidx.appcompat.widget.Toolbar,
        nav_view: NavigationView?
    ): NavigationView? {
        val drawerLayout = (context as Activity).findViewById<DrawerLayout>(R.id.drawer_layout)
        val toggle = ActionBarDrawerToggle(
            context, drawerLayout, toolbarMain, R.string.navigation_drawer_open, R.string.navigation_drawer_close
        )

        drawerLayout.addDrawerListener(toggle)
        toggle.isDrawerIndicatorEnabled = true
        toggle.syncState()

        nav_view?.itemIconTintList = null
        nav_view?.setNavigationItemSelectedListener(this)
        return nav_view
    }


}

