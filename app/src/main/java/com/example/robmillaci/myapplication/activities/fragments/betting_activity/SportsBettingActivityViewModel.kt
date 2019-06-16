package com.example.robmillaci.myapplication.activities.fragments.betting_activity

import androidx.lifecycle.ViewModel
import com.example.robmillaci.myapplication.adapters.BetsAdapter
import com.example.robmillaci.myapplication.pojos.SportsEvent

class SportsBettingActivityViewModel : ViewModel() {
    val mModel: SportsBettingModel = SportsBettingModel()
    //retrieve the data from the model related to the specific sports object clicked - returning all relevant betting data

    internal fun getDummyData(sportsObject: SportsEvent): MutableList<BetsAdapter.MyGroupItem> {
        //this data is faked ...obviously :)
        return mModel.retrieveDataForObject(sportsObject)
    }
}