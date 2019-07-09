package com.example.robmillaci.myapplication.activities.fragments.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.activities.fragments.home_activity.FragmentCommunication
import com.example.robmillaci.myapplication.activities.fragments.home_activity.HomeActivityViewModel
import com.example.robmillaci.myapplication.activities.fragments.home_activity.SPORTS_TAB
import com.example.robmillaci.myapplication.adapters.EventAdapter
import com.example.robmillaci.myapplication.miscs.CustomLinearLayoutManager
import kotlinx.android.synthetic.main.loading_progress.*
import kotlinx.android.synthetic.main.sports_data_fragment.*
import java.lang.ref.WeakReference

/**
 * The home fragment displaying recyclerview data for either sports or racing events
 * @param listener - the activity this fragment is attached to in order to communicate between
 */
class HomeFragment(private val listener: ActivityListener) : Fragment(), FragmentCommunication {
    private lateinit var mViewModel: HomeActivityViewModel //the view model associated with this fragment/view
    private lateinit var sAdapter: EventAdapter //the adapter to populate the recyclerview with sports or racing data

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.sports_data_fragment, container, false)

    }


    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        mViewModel = ViewModelProviders.of(this.activity!!).get(HomeActivityViewModel::class.java)

        sAdapter = EventAdapter(WeakReference(context!!), mViewModel.sectionDataSports)

        /*
        Observe the sports data within the view model for changes, updating the adapter with any changes when they occur
         */
        mViewModel.sectionDataSports.observe(this, Observer {
            sports_loading_progress_view.visibility = View.GONE
            recyclerViewData.visibility = View.VISIBLE
            sAdapter.setData(it)
            sAdapter.notifyDataSetChanged()
        })


        /*
        Observe the racing data within the view model for changes, updating the adapter with any changes when they occur
        */
        mViewModel.sectionDataRacing.observe(this, Observer {
            sports_loading_progress_view.visibility = View.GONE
            recyclerViewData.visibility = View.VISIBLE
            sAdapter.setData(it)
            sAdapter.notifyDataSetChanged()
        })


//        lifecycle.addObserver(mViewModel)


        recyclerViewData.apply {
            this.layoutManager = CustomLinearLayoutManager(context)
            this.adapter = sAdapter
            this.isNestedScrollingEnabled = false
        }

        recyclerViewData.itemAnimator?.changeDuration = 0

        /*
        tell the activity to initially display the viewpager items for sports data. The subsequent on click tab events related to viewpager updates
        are handled by the home activity hosting the viewpager
         */
        listener.updateViewPager(SPORTS_TAB)
    }


    /**
     *  Method called when the sports tab is clicked. This displays loading progress bars if the viewmodel doesn't currently hold any sports data.
     *  If the data already exists, loading is not shown and the data is displayed straight away while also fetching the latest data.
     *  This method also updates the home icons
     */
    private fun createSportsRecyclerViewsData() {
        if (mViewModel.sectionDataSports.value == null) {
            showSportsLoading()
        }
        sAdapter.setData(mViewModel.sectionDataSports.value)
        sAdapter.notifyDataSetChanged()
        updateHomeIcons(1)
        mViewModel.getSportsData()
    }


    /**
     *  Method called when the racing tab is clicked. This displays loading progress bars if the viewmodel doesn't currently hold any racing data.
     *  If the data already exists, loading is not shown and the data is displayed straight away while also fetching the latest data.
     *  This method also updates the home icons
     */
    private fun createRacingRecyclerViewsData() {
        if (mViewModel.sectionDataRacing.value == null) {
            showRacingLoading()
        }
        sAdapter.setData(mViewModel.sectionDataRacing.value)
        sAdapter.notifyDataSetChanged()
        updateHomeIcons(0)
        mViewModel.getRacingData()

    }

    /**
     * Displays and updates relevant 'loading' views for loading sports data
     */
    private fun showSportsLoading() {
        sports_loading_progress_view.visibility = View.VISIBLE
        recyclerViewData.visibility = View.GONE
        loadingTitleOne.text = "Upcoming Sports"
        loadingTitleTwo.text = "Bet Live"

    }


    /**
     * Displays and updates relevant 'loading' views for loading racing data
     */
    private fun showRacingLoading() {
        sports_loading_progress_view.visibility = View.VISIBLE
        recyclerViewData.visibility = View.GONE
        loadingTitleOne.text = "Upcoming Races"
        loadingTitleTwo.text = "Bet Live"
    }


    //this data looks like it is downloaded and updated on tab selected/from cache
    //fake the data here...
    private fun updateHomeIcons(id: Int) {
        when (id) {
            1 -> {
                multibuildebtn.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        context?.resources?.getDrawable(R.drawable.multibuilder_button),
                        null,
                        null
                    )
                    this.setText("MultiBuilder")
                }

                game2btn.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        context?.resources?.getDrawable(R.drawable.game_two_button),
                        null,
                        null
                    )
                    this.setText("Game 2")
                }

                originbtn.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        context?.resources?.getDrawable(R.drawable.origin_one_button),
                        null,
                        null
                    )
                    this.setText("Origin 1")
                }
            }


            0 -> {

                multibuildebtn.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        context?.resources?.getDrawable(R.drawable.horses_icon),
                        null,
                        null
                    )
                    this.setText("Horses")
                }

                game2btn.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        context?.resources?.getDrawable(R.drawable.greys_icon),
                        null,
                        null
                    )
                    this.setText("Greys")
                }

                originbtn.apply {
                    setCompoundDrawablesWithIntrinsicBounds(
                        null,
                        context?.resources?.getDrawable(R.drawable.harnes_icon),
                        null,
                        null
                    )
                    this.setText("Harness")
                }

            }
        }

    }


    override fun tabSelected(pos: Int) {
        when (pos) {
            0 -> createRacingRecyclerViewsData()
            1 -> createSportsRecyclerViewsData()
        }
    }


    interface ActivityListener {
        fun updateViewPager(tag: String)
    }


}