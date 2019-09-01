package com.example.robmillaci.myapplication.activities.fragments.home_activity

import android.annotation.SuppressLint
import android.content.SharedPreferences
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.robmillaci.myapplication.NavTools
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.activities.fragments.fragments.HomeFragment
import com.example.robmillaci.myapplication.adapters.BetSlipAdapter
import com.example.robmillaci.myapplication.adapters.ViewPagerAdapter
import com.example.robmillaci.myapplication.extension_functions.pulse
import com.example.robmillaci.myapplication.miscs.CallingType
import com.example.robmillaci.myapplication.miscs.SpacesItemDecoration
import com.google.android.material.navigation.NavigationView
import com.google.android.material.tabs.TabLayout
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.bet_slip_layout.*
import kotlinx.android.synthetic.main.collapsable_toolbar.*
import kotlinx.android.synthetic.main.tab_layout.*
import java.lang.ref.WeakReference


/**
 * Activity related constants - public static final values in Java world
 */
const val SPORTS_TAB = "Sports"
const val RACING_TAB = "Racing"
const val TAB_SELECTED_STATE = "TabSelected"
const val VIEW_PAGER_UPDATE_RATE = 5000L
const val BET_SLIP_SHARED_PREF = "betslip"


/**
 * This class is reponsible for the home activity view. Hosting the view pager and the 2 navigation views. Creates the home fragment, handles the view pager
 * updates and the tab listener
 */
class HomeActivityView : AppCompatActivity(), HomeFragment.ActivityListener, BetSlipDrawerOpened {
    private lateinit var updateSportsViewPager: Runnable //Runnable to handle updating the sports view pager images
    private lateinit var updateRacingViewPager: Runnable //Runnable to handle updating the racing view pager images
    private val handler = Handler() //The handler that allows processing of the viewpager Runnable objects.
    private var listener: FragmentCommunication? = null //The callback to the hosted fragment
    private var navigationView: NavigationView? = null //Main navigation view
    private var betSlipNavigationView: NavigationView? = null //bet slip navigation view
    private lateinit var mViewModel: HomeActivityViewModel //the view model associated with this activity
    private lateinit var preferenceManager: SharedPreferences //Shared preference instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        setSupportActionBar(toolbarMain)
        supportActionBar?.apply {
            this.setDisplayHomeAsUpEnabled(true)
            this.setHomeButtonEnabled(true)
            this.setDisplayShowTitleEnabled(false)
        }

        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)

        //Create the viewmodel for this activity
        mViewModel = ViewModelProviders.of(this).get(HomeActivityViewModel::class.java)

        //update the betslip with the stores betslip value
        mViewModel.betSlipValue.value = preferenceManager.getInt(BET_SLIP_SHARED_PREF, 0)

        //create the navigation views
        navigationView = NavTools(this).configureNavView(this, toolbarMain, nav_view)
        betSlipNavigationView = NavTools(this).configureBetSlipView(this, betSlip_nav_view)


        createFragment()
        createViewPagers()
        createTabListener()
        observeBetSlip()
    }


    /**
     * Observe the betslip such that when the viewmodels live data regarding the betslip value changes, the betslip is animated and sharedpreferences
     * is updated.
     * Also handles updating of the bet objects added to the betslip navigation view - i.e when these are removed from the betslip, the view models live data
     * betSlipItems is updated and triggers a call to the observer to update the recyclerview
     */
    @SuppressLint("ApplySharedPref")
    private fun observeBetSlip() {
        mViewModel.betSlipValue.observe(this, Observer {
            findViewById<TextView>(R.id.bet_slip_amount).text = it.toString()
            if (mViewModel.betSlipValue.value == 0) {
                drawer_layout.closeDrawer(GravityCompat.END)
            }
            animateBetSlip()
            preferenceManager.edit().putInt(BET_SLIP_SHARED_PREF, it).commit()
        })


        mViewModel.betSlipItems?.observe(this, Observer {
            try {
                betSlipNavigationView?.findViewById<RecyclerView>(R.id.betslip_recycler_view)?.apply {
                    this.adapter = BetSlipAdapter(
                        WeakReference(context),
                        it
                    )
                    this.layoutManager = LinearLayoutManager(context)
                    this.addItemDecoration(SpacesItemDecoration(10, CallingType.BET_CALLING_TYPE))
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        })
    }


    /*
    Save the users currently selected tab when on Pause is called. When on resume is then later called the user is returned to the tab they
    had currently selected
     */
    @SuppressLint("ApplySharedPref")
    override fun onPause() {
        super.onPause()
        PreferenceManager.getDefaultSharedPreferences(this).edit().also {
            it.putInt(TAB_SELECTED_STATE, tabLayout.selectedTabPosition)
            it.commit()
        }
    }


    /*
    On resume returns the user to the tab they had selected previously
     */
    override fun onResume() {
        super.onResume()
        tabLayout.getTabAt(PreferenceManager.getDefaultSharedPreferences(this).getInt(TAB_SELECTED_STATE, 1))?.select()
        mViewModel.betSlipValue.value = preferenceManager.getInt(BET_SLIP_SHARED_PREF, 0)
    }


    /**
     * Create the fragment that this activity is hosting - The home fragment
     */
    private fun createFragment() {
        val fragment = HomeFragment(this)
        setListener(fragment)

        supportFragmentManager.beginTransaction().replace(
            R.id.main_fragment,
            fragment
        ).commit()

    }


    /**
     * Create the tab on click listeners which update the view pager with the relevant images and also call to the hosted fragment view the listener
     * instance that a tab has been selected in order to update any displayed data
     */
    private fun createTabListener() {

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabReselected(p0: TabLayout.Tab?) {
                onTabSelected(p0)
            }

            override fun onTabUnselected(p0: TabLayout.Tab?) {
            }

            override fun onTabSelected(tab: TabLayout.Tab?) {
                when {
                    tab?.position == 1 -> {
                        listener?.tabSelected(1)
                        updateViewPager(SPORTS_TAB)
                    }
                    tab?.position == 0 -> {
                        listener?.tabSelected(0)
                        updateViewPager(RACING_TAB)
                    }
                }
            }

        })
    }


    /**
     * Handles the creation of the view pagers - These images change frequently in the real app and are most likely hosted and populated
     * via Picasso - Here they are a static list of images that simulate the real behaviour of the app.
     */
    private fun createViewPagers() {
        val sportsImages = arrayOf(
            R.drawable.sports_image_1,
            R.drawable.sports_image_2,
            R.drawable.sports_image_3,
            R.drawable.sports_image_4
        )
        val racingImages = arrayOf(
            R.drawable.racing_image1,
            R.drawable.racing_image2,
            R.drawable.racing_image3,
            R.drawable.racing_image4
        )

        view_pager_sports.also {
            it.adapter =
                ViewPagerAdapter(sportsImages, WeakReference(this))
            it.offscreenPageLimit = sportsImages.size
        }

        view_pager_racing.also {
            it.adapter = ViewPagerAdapter(racingImages, WeakReference(this))
            it.offscreenPageLimit = racingImages.size
        }

        view_pager_racing.visibility = View.GONE

        setTimer()
        startViewPagerUpdates()
    }


    /**
     * Animates the betslip to pulse when items are added or removed
     */
    private fun animateBetSlip() {
        bet_slip.pulse()
        bet_slip_amount.pulse()
    }


    /**
     * Sets a handler to post a runnable every VIEW_PAGER_UPDATE_RATE milliseconds.
     * The runnable simply updates the current displayed item in the view pager
     */
    private fun setTimer() {
        updateSportsViewPager = Runnable {
            if (view_pager_sports.currentItem + 1 == view_pager_sports.adapter?.count) {
                view_pager_sports.currentItem = 0
            } else {
                view_pager_sports.currentItem = view_pager_sports.currentItem + 1
            }
            handler.postDelayed(updateSportsViewPager, VIEW_PAGER_UPDATE_RATE)
        }

        updateRacingViewPager = Runnable {
            if (view_pager_racing.currentItem + 1 == view_pager_racing.adapter?.count) {
                view_pager_racing.currentItem = 0
            } else {
                view_pager_racing.currentItem = view_pager_racing.currentItem + 1
            }
            handler.postDelayed(updateRacingViewPager, VIEW_PAGER_UPDATE_RATE)
        }
    }


    /**
     * Begins the view pager updates
     */
    private fun startViewPagerUpdates() {
        Handler().postDelayed({ updateSportsViewPager.run() }, VIEW_PAGER_UPDATE_RATE)
        Handler().postDelayed({ updateRacingViewPager.run() }, VIEW_PAGER_UPDATE_RATE)
    }


    /**
     * Changes the viewpager depending on which tab has been selected
     */
    override fun updateViewPager(tag: String) {
        when (tag) {
            SPORTS_TAB -> {
                view_pager_racing.visibility = View.INVISIBLE
                view_pager_sports.apply {
                    this.visibility = View.VISIBLE
                    indicator.setViewPager(this)
                }
            }
            RACING_TAB -> {
                view_pager_sports.visibility = View.INVISIBLE
                view_pager_racing.apply {
                    this.visibility = View.VISIBLE
                    indicator.setViewPager(this)
                }
            }
        }
    }


    /**
     * Sets the lsitener for this activity, this is used for communication between this activity and the hosted fragment
     */
    private fun setListener(listener: FragmentCommunication) {
        this.listener = listener
    }

    override fun onBackPressed() {
        val drawerLayout: DrawerLayout = findViewById(R.id.drawer_layout)
        when {
            drawerLayout.isDrawerOpen(GravityCompat.START) -> drawerLayout.closeDrawer(GravityCompat.START)
            drawerLayout.isDrawerOpen(GravityCompat.END) -> drawerLayout.closeDrawer(GravityCompat.END)
            else -> super.onBackPressed()
        }

    }


    /**
     * Get all bets from the model when the betslip is opened
     */
    override fun onOpenBetSlip() {
     //   mViewModel.getAllBets()
    }


}

interface FragmentCommunication {
    fun tabSelected(pos: Int)
}

interface BetSlipDrawerOpened {
    fun onOpenBetSlip()
}