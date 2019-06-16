package com.example.robmillaci.myapplication.activities.fragments.betting_activity

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
import com.example.robmillaci.myapplication.adapters.BetSlipAdapter
import com.example.robmillaci.myapplication.adapters.BetsAdapter
import com.example.robmillaci.myapplication.extension_functions.pulse
import com.example.robmillaci.myapplication.extension_functions.translationsSlide
import com.example.robmillaci.myapplication.activities.fragments.home_activity.BET_SLIP_SHARED_PRED
import com.example.robmillaci.myapplication.activities.fragments.home_activity.BetSlipDrawerOpened
import com.example.robmillaci.myapplication.activities.fragments.home_activity.HomeActivityViewModel
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


class SportsBettingActivityView : AppCompatActivity(), BetSlipDrawerOpened, BetsAdapter.IViewbetClickedListener  {
    private var sportsObject: SportsEvent? = null
    private lateinit var recyclerViewItems: MutableList<BetsAdapter.MyGroupItem>
    private lateinit var mSportsBettingViewModel: SportsBettingActivityViewModel
    private lateinit var mHomeActivityViewModel: HomeActivityViewModel
    private var navigationView: NavigationView? = null
    private var betSlipNavigationView: NavigationView? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sports_betting)

        supportActionBar?.setDisplayShowTitleEnabled(true)
        supportActionBar?.setTitle("")

        //todo clean this up - recreating 2 NavTool objects - only one is needed
        navigationView = NavTools(this).configureNavView(this, toolbarSportsBetting, betSlip_nav_view)
        betSlipNavigationView = NavTools(this).configureBetSlipView(this, betSlip_nav_view)

        getSportsObject(intent.extras)

        mSportsBettingViewModel = ViewModelProviders.of(this).get(SportsBettingActivityViewModel::class.java)
        mHomeActivityViewModel = ViewModelProviders.of(this).get(HomeActivityViewModel::class.java)

        createRecyclerViewData()
        createRecyclerView()
        addAppbarOffsetListener()
        createScrollListener()
        createObservers()

    }

    //todo make a global observer
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
                Log.d("BETSLIP","${e.printStackTrace()}")
                e.printStackTrace()
            }
        })

        mHomeActivityViewModel.betSlipValue.observe(this, Observer {
            findViewById<TextView>(R.id.bet_slip_amount).text = it.toString()
            if (mHomeActivityViewModel.betSlipValue.value == 0) {
                drawer_layout.closeDrawer(GravityCompat.END)
            }

            //todo combine the two animate betslip methods somehow
            bet_slip.pulse()
            bet_slip_amount.pulse()
            PreferenceManager.getDefaultSharedPreferences(this).edit().putInt(BET_SLIP_SHARED_PRED, it).commit()
        })
    }


    private fun createScrollListener() {
        var allowedToTranslateOut = false
        var allowedToTranslateIn = true
        val origionalPos = image_slide.top

        betting_scroll_view.setOnScrollChangeListener { v: NestedScrollView?, scrollX: Int, scrollY: Int, oldScrollX: Int, oldScrollY: Int ->
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


    private fun createRecyclerViewData() {
        if (sportsObject != null) recyclerViewItems = mSportsBettingViewModel.getDummyData(sportsObject!!)
    }


    private fun addAppbarOffsetListener() {
        val drawableArrow = resources.getDrawable(android.R.drawable.btn_dropdown)
        betting_app_bar.addOnOffsetChangedListener(AppBarLayout.OnOffsetChangedListener { a, verticalOffset ->
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


    private fun getSportsObject(extras: Bundle?) {
        sportsObject = extras?.getSerializable("object") as SportsEvent
        val thisSportsObject = sportsObject

        event_date_time.text = thisSportsObject?.getStartTime()


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
        Log.d("BETSLIP","betslip opened")
        mHomeActivityViewModel.getAllBets()
    }

    override fun betClicked(view: View, eventObject: IEventObject) {
        if (view.tag != getString(R.string.clicked_button)) {
            view.setBackgroundResource(R.drawable.ripple_button_blue_pressed)
            odds_tv.setTextColor(resources.getColor(android.R.color.white))
            view.setTag(getString(R.string.clicked_button))

            eventObject.setchosenOdds(odds_tv.text.toString())
            eventObject.setChosenOutcome(child_tv.text.toString())

            mHomeActivityViewModel.updateBetSlip(eventObject, 0, true)

        } else {
            view.setBackgroundResource(R.drawable.ripple_button_blue)
            view.findViewById<TextView>(R.id.odds_tv).setTextColor(resources.getColor(android.R.color.black))
            view.setTag(getString(R.string.unclicked_button))
            mHomeActivityViewModel.updateBetSlip(eventObject, 0, false)
        }
    }

}

