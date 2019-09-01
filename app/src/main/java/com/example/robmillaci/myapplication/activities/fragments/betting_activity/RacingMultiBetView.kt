package com.example.robmillaci.myapplication.activities.fragments.betting_activity

import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.robmillaci.myapplication.NavTools
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.activities.fragments.MainApplication
import com.example.robmillaci.myapplication.activities.fragments.home_activity.BET_SLIP_SHARED_PREF
import com.example.robmillaci.myapplication.activities.fragments.home_activity.BetSlipDrawerOpened
import com.example.robmillaci.myapplication.activities.fragments.home_activity.HomeActivityViewModel
import com.example.robmillaci.myapplication.adapters.BetSlipAdapter
import com.example.robmillaci.myapplication.adapters.BetsAdapter
import com.example.robmillaci.myapplication.extension_functions.pulse
import com.example.robmillaci.myapplication.extension_functions.translationsSlide
import com.example.robmillaci.myapplication.miscs.CallingType
import com.example.robmillaci.myapplication.miscs.SpacesItemDecoration
import com.example.robmillaci.myapplication.pojos.IEventObject
import com.example.robmillaci.myapplication.pojos.RacingEvent
import com.example.robmillaci.myapplication.pojos.SpecificTypes
import com.google.android.material.navigation.NavigationView
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager
import kotlinx.android.synthetic.main.activity_sports_betting.*
import kotlinx.android.synthetic.main.app_bar_sports_betting.*
import kotlinx.android.synthetic.main.bet_slid_nav_view_main_layout.*
import kotlinx.android.synthetic.main.bet_slip_layout.*
import kotlinx.android.synthetic.main.content_racing_multi.*
import kotlinx.android.synthetic.main.multi_racing_toolbar.*
import kotlinx.android.synthetic.main.racing_multi_main_content.*
import java.lang.ref.WeakReference

class RacingMultiBetView : AppCompatActivity(), BetSlipDrawerOpened, BetsAdapter.IViewbetClickedListener {

    private var mRacingEvent: RacingEvent? = null //the specific race parsed from Json response from DB
    private var mRecyclerViewItems: MutableList<BetsAdapter.MyGroupItem> =
        ArrayList<BetsAdapter.MyGroupItem>() //recycler view to display the types of bets
    private lateinit var mRacingMutliBetViewModel: RacingMultiBetViewModel //the view model for this view
    private lateinit var mHomeActivityViewModel: HomeActivityViewModel //the home activity view model
    private var mBetSlipNavigationView: NavigationView? = null //the bet split navigation view across all activities
    private lateinit var mPreferenceManager: SharedPreferences //Shared preferences instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_racing_mutli_bet_view)
        setSupportActionBar(toolbarMultiRacing)
        title = ""
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        mPreferenceManager = PreferenceManager.getDefaultSharedPreferences(this)

        /*
        Create the navigation views for this activity
       */
        val navTools = NavTools(this)
        mBetSlipNavigationView = navTools.configureBetSlipView(this, betSlip_nav_view)


        /*
        Get the sports object passed into this activity - This is the event on which all bets will be placed
         */
        getRacingEvent(intent.extras)
//
        createViewModels()
        createRecyclerViewData()
        createObservers()
        bet_slip_amount.text = MainApplication.sMultiRacingBets.size.toString()
    }

    private fun createObservers() {
        mRacingMutliBetViewModel.mNumberOfMultiBets.observe(this, Observer {
            multi_bet_popup.visibility = View.VISIBLE

        })

        mHomeActivityViewModel.betSlipItems?.observe(this, Observer {
            try {
                mBetSlipNavigationView?.findViewById<RecyclerView>(R.id.betslip_recycler_view)?.apply {
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

        mHomeActivityViewModel.betSlipValue.observe(this, Observer {
            findViewById<TextView>(R.id.bet_slip_amount).text = it.toString()
            if (mHomeActivityViewModel.betSlipValue.value == 0) {
                drawer_layout.closeDrawer(GravityCompat.END)
            }

            bet_slip.pulse()
            bet_slip_amount.pulse()
            mPreferenceManager.edit().putInt(BET_SLIP_SHARED_PREF, it).commit()
        })
    }



/*
Scroll listener to show/hide the bottom "Same game multi" bar.
//todo this needs work!
*/
private fun createScrollListener() {
    var allowedToTranslateOut = false
    var allowedToTranslateIn = true
    val origionalPos = image_slide.top

    horses_recycler_view.addOnScrollListener(object : RecyclerView.OnScrollListener() {
        override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
            super.onScrolled(recyclerView, dx, dy)
            if (dy > 0) {
                image_slide.translationsSlide(2 * image_slide.width.toFloat(), 0f)
            } else {
                image_slide.translationsSlide(origionalPos.toFloat(), 1f)
            }
        }
    })

//            betting_scroll_view.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
//                if (oldScrollY < scrollY && allowedToTranslateOut) { //we are scrolling up and so hide the floating view
//                    image_slide.translationsSlide(2 * image_slide.width.toFloat(), 0f)
//                    allowedToTranslateIn = true
//                    allowedToTranslateOut = false
//
//                } else if (allowedToTranslateIn) { //we are scrolling down and so show the floating view
//                    image_slide.translationsSlide(origionalPos.toFloat(), 1f)
//                    allowedToTranslateIn = false
//                    allowedToTranslateOut = true
//                }
//            }
}


private fun getRacingEvent(extras: Bundle?) {
    //  mRacingEvent = extras?.getSerializable("object") as RacingEvent ?:
    mRacingEvent =
        RacingEvent(1, SpecificTypes.HORSES, "Ballinrobe", "Race 5", "12/07/2019 14:10", "12/07/2019 18:00")
}

private fun createRecyclerViewData() {
    if (mRacingEvent != null) {
        val thisRacingEvent = mRacingEvent!!
        mRecyclerViewItems = mRacingMutliBetViewModel.getDummyData(thisRacingEvent)
        createRecyclerView()
    }
}

private fun createRecyclerView() {
    val expMgr = RecyclerViewExpandableItemManager(null)
    val decorator = SpacesItemDecoration(15, CallingType.BET_CALLING_TYPE)
    horses_recycler_view.also {
        it.isNestedScrollingEnabled = false
        it.layoutManager = LinearLayoutManager(this)

        val betsAdapter = BetsAdapter(mRecyclerViewItems, decorator, this).apply {
            this.setViewModel(mRacingMutliBetViewModel)
        }

        it.adapter = (expMgr.createWrappedAdapter(betsAdapter))
        (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
        it.addItemDecoration(decorator)
        expMgr.attachRecyclerView(it)
    }
}

/*
Create the viewmodels associated with this activity, also restore the bet slip value
*/
private fun createViewModels() {
    mRacingMutliBetViewModel = ViewModelProviders.of(this).get(RacingMultiBetViewModel::class.java)
    mHomeActivityViewModel = ViewModelProviders.of(this).get(HomeActivityViewModel::class.java)
    mHomeActivityViewModel.betSlipValue.value = mPreferenceManager.getInt(BET_SLIP_SHARED_PREF, 0)
}


override fun betClicked(view: View, eventObject: IEventObject, betName: String) {
    //clicked on a betting item - show the bottom bar with info
    if (view.tag != "clicked") {
        view.tag = "clicked"
        view.setBackgroundResource(R.drawable.bet_btn_pressed)
        view.findViewById<TextView>(R.id.odds_tv).setTextColor(resources.getColor(android.R.color.white))
    } else {
        removeTheBet(view)
    }
}

override fun removeTheBet(view: View) {
    view.setBackgroundResource(R.drawable.bet_btn_unpressed)
    view.findViewById<TextView>(R.id.odds_tv).setTextColor(resources.getColor(android.R.color.black))
    view.tag = getString(R.string.unclicked_button)
}

override fun onOpenBetSlip() {
    if (drawer_layout.isDrawerOpen(GravityCompat.END)) {
        drawer_layout.closeDrawer(GravityCompat.END);
    } else {
        drawer_layout.openDrawer(GravityCompat.END);
    }
}

override fun onOptionsItemSelected(item: MenuItem?): Boolean {
    return true
}
}
