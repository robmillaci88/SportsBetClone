@file:Suppress("DEPRECATION")

package com.example.robmillaci.myapplication.activities.fragments.betting_activity

import android.annotation.SuppressLint
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.preference.PreferenceManager
import android.util.Log
import android.view.View
import android.widget.ArrayAdapter
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.core.widget.NestedScrollView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.SimpleItemAnimator
import com.example.robmillaci.myapplication.NavTools
import com.example.robmillaci.myapplication.R
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
import com.example.robmillaci.myapplication.pojos.SportsEvent
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.h6ah4i.android.widget.advrecyclerview.expandable.RecyclerViewExpandableItemManager
import kotlinx.android.synthetic.main.activity_sports_betting.*
import kotlinx.android.synthetic.main.app_bar_sports_betting.*
import kotlinx.android.synthetic.main.bet_slip_layout.*
import kotlinx.android.synthetic.main.bet_type_children_view.*
import kotlinx.android.synthetic.main.collapsable_toolbar_sports_betting.*
import kotlinx.android.synthetic.main.content_sports_betting.*
import java.lang.ref.WeakReference

/**
 * The view class for a specific sports betting activity
 */
class SportsBettingActivityView : AppCompatActivity(), BetSlipDrawerOpened, BetsAdapter.IViewbetClickedListener {
    override fun removeTheBet(view: View) {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }


    private var sportsObject: SportsEvent? = null //the specific sports betting object
    private lateinit var recyclerViewItems: MutableList<BetsAdapter.MyGroupItem> //recyclew view to display the types of bets
    private lateinit var mSportsBettingViewModel: SportsBettingActivityViewModel //the view model for this view
    private lateinit var mHomeActivityViewModel: HomeActivityViewModel //the home activity view model
    private var navigationView: NavigationView? = null //the main navigation view across all activities
    private var betSlipNavigationView: NavigationView? = null //the bet split navigation view across all activities
    private lateinit var preferenceManager: SharedPreferences //Shared preferences instance

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports_betting)

        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.title = ""
        preferenceManager = PreferenceManager.getDefaultSharedPreferences(this)

        /*
        Create the navigation views for this activity
         */
        val navTools = NavTools(this)
        navigationView = navTools.configureNavView(this, toolbarSportsBetting, betSlip_nav_view)
        betSlipNavigationView = navTools.configureBetSlipView(this, betSlip_nav_view)


        /*
        Get the sports object passed into this activity - This is the event on which all bets will be placed
         */
        getSportsObject(intent.extras)


        /*
        Create the viewmodels associated with this activity, also restore the bet slip value
         */
        mSportsBettingViewModel = ViewModelProviders.of(this).get(SportsBettingActivityViewModel::class.java)
        mHomeActivityViewModel = ViewModelProviders.of(this).get(HomeActivityViewModel::class.java)
        mHomeActivityViewModel.betSlipValue.value = preferenceManager.getInt(BET_SLIP_SHARED_PREF, 0)


        createRecyclerViewData()
        createOnClickListeners()
        createRecyclerView()
        addAppbarOffsetListener()
        createScrollListener()
        createObservers()

        /*
        Return all bets that a user has previously added to the bet slip - This wouldnt be saved in the real application due to
        the fact that bets can expire / change but for demo purposes we have saved the users previously added bets
         */
        // mHomeActivityViewModel.getAllBets()
    }

    private fun createOnClickListeners() {
        image_slide.setOnClickListener {
            startActivity(Intent(this, RacingMultiBetView::class.java))
        }
    }


    /*
    Create observers to observe the betslip items which recreate the recyclerview when new bets are added
    Also observe the number of bets added to the bet slip in order to provide UI animation when adding a bet and updating shared preferences
     */
    @SuppressLint("ApplySharedPref")
    private fun createObservers() {
        mHomeActivityViewModel.betSlipItems?.observe(this, Observer {
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

        mHomeActivityViewModel.betSlipValue.observe(this, Observer {
            findViewById<TextView>(R.id.bet_slip_amount).text = it.toString()
            if (mHomeActivityViewModel.betSlipValue.value == 0) {
                drawer_layout.closeDrawer(GravityCompat.END)
            }

            bet_slip.pulse()
            bet_slip_amount.pulse()
            preferenceManager.edit().putInt(BET_SLIP_SHARED_PREF, it).commit()
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

        betting_scroll_view.setOnScrollChangeListener { _: NestedScrollView?, _: Int, scrollY: Int, _: Int, oldScrollY: Int ->
            if (oldScrollY < scrollY && allowedToTranslateOut) { //we are scrolling up and so hide the floating view
                image_slide.translationsSlide(2 * image_slide.width.toFloat(), 0f)
                allowedToTranslateIn = true
                allowedToTranslateOut = false

            } else if (allowedToTranslateIn) { //we are scrolling down and so show the floating view
                image_slide.translationsSlide(origionalPos.toFloat(), 1f)
                allowedToTranslateIn = false
                allowedToTranslateOut = true
            }
        }
    }


    /*
    Display the dummy betting items
     */
    private fun createRecyclerView() {
        val expMgr = RecyclerViewExpandableItemManager(null)
        val decorator = SpacesItemDecoration(10, CallingType.BET_CALLING_TYPE)
        sportsevent_betting_recyclerview.also {
            it.isNestedScrollingEnabled = false
            it.layoutManager = LinearLayoutManager(this)
            it.adapter = (expMgr.createWrappedAdapter(BetsAdapter(recyclerViewItems, decorator, this)))
            (it.itemAnimator as SimpleItemAnimator).supportsChangeAnimations = false
            it.addItemDecoration(decorator)
            expMgr.attachRecyclerView(it)
        }
    }


    /*
    Creates dummy data to display for this activity. Real world data would likely come from a JSON feed from a separate integrated system
     */
    private fun createRecyclerViewData() {
        if (sportsObject != null) recyclerViewItems = mSportsBettingViewModel.getDummyData(sportsObject!!)
    }


    /*
    Apply alpha property to the spinner (slowly transition to invisible) when the screen is scrolled
    This is for a nice UI/UX effect
     */
    private fun addAppbarOffsetListener() {
        betting_app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { _, verticalOffset ->
            val range = (-betting_app_bar.totalScrollRange)
            val alphaToApply = 1 - verticalOffset.toFloat() / range.toFloat()

            view_pager_spinner.alpha = alphaToApply
            view_pager_spinner.background.alpha = (255 * (alphaToApply)).toInt()
            event_date_time.alpha = alphaToApply

            if (view_pager_spinner.alpha == 0f) {
                toolbar_spinner.visibility = View.VISIBLE
            } else {
                toolbar_spinner.visibility = View.GONE
            }
        })

    }


    /**
     * Get the sports object passed into the bundle when creating this activity
     */
    private fun getSportsObject(extras: Bundle?) {
        sportsObject = extras?.getSerializable("object") as SportsEvent
        val thisSportsObject = sportsObject

        event_date_time.text = thisSportsObject?.startTime

        //Fake data into the spinner drop down list
        val spinnerArrayAdapter = ArrayAdapter<String>(
            this, R.layout.custom_spinner_layout,
            arrayOf(thisSportsObject?.gameName)
        )
        spinnerArrayAdapter.setDropDownViewResource(
            android.R.layout
                .simple_spinner_dropdown_item
        )
        view_pager_spinner.adapter = spinnerArrayAdapter
        toolbar_spinner.adapter = spinnerArrayAdapter

    }

    override fun onOpenBetSlip() {
//        mHomeActivityViewModel.getAllBets()
    }

    /**
     * On Click event when a betting item is clicked
     */
    override fun betClicked(view: View, eventObject: IEventObject, betName: String) {
        Log.d("REMOVE","now in the activity")
        if (view.tag != getString(R.string.clicked_button)) {
            Log.d("REMOVE","matched the if")

            view.setBackgroundResource(R.drawable.bet_btn_pressed)
            odds_tv.setTextColor(resources.getColor(android.R.color.white))
            view.tag = getString(R.string.clicked_button)


            eventObject.chosenOdds = odds_tv.text.toString()
            eventObject.chosenOutcomes = child_tv.text.toString()
            eventObject.betName = betName

            //  mHomeActivityViewModel.updateBetSlip(eventObject, 0, true)

        } else {
            Log.d("REMOVE","matched the else")

            view.setBackgroundResource(R.drawable.bet_btn_unpressed)
            odds_tv.setTextColor(resources.getColor(android.R.color.black))
            view.tag = getString(R.string.unclicked_button)
            //  mHomeActivityViewModel.updateBetSlip(eventObject, 0, false)
        }
    }

}

