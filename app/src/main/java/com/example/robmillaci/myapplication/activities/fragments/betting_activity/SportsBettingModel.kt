package com.example.robmillaci.myapplication.activities.fragments.betting_activity

import com.example.robmillaci.myapplication.adapters.BetsAdapter
import com.example.robmillaci.myapplication.pojos.SportsEvent

/**
 * A dummy model class that acts as though it is retrieving data from JSON/source system
 */
class SportsBettingModel {

    internal fun retrieveDataForObject(sportsObject: SportsEvent): MutableList<BetsAdapter.MyGroupItem> {
        //RXJava calls to Json API / Database / file ...whatever the data source is to grab the data asynchronously
        //fake the data here ...
        return mutableListOf<BetsAdapter.MyGroupItem>().apply {
            this.add(BetsAdapter.MyGroupItem(0, "Big Win Little Win",sportsObject).also {
                (it.children as MutableList).add(BetsAdapter.MyChildItem(0, "Home 1-39", 3.00,1))
                (it.children).add(BetsAdapter.MyChildItem(1, "Home 40+", 13.00,2))
                (it.children).add(BetsAdapter.MyChildItem(2, "Draw", 56.00,3))
                (it.children).add(BetsAdapter.MyChildItem(3, "Away 1-39", 2.10,4))
                (it.children).add(BetsAdapter.MyChildItem(4, "Away 40+", 4.50,5))
            })

            this.add(BetsAdapter.MyGroupItem(1, "Time First Goal Kicked",sportsObject).also {
                (it.children as MutableList).add(BetsAdapter.MyChildItem(0, "0:00 - 1:00", 7.00,1))
                (it.children).add(BetsAdapter.MyChildItem(1, "1:01 - 2:00", 4.50,2))
                (it.children).add(BetsAdapter.MyChildItem(2, "2:01 - 3:00", 5.50,3))
                (it.children).add(BetsAdapter.MyChildItem(3, "3:01 - 4:00", 6.50,4))
                (it.children).add(BetsAdapter.MyChildItem(4, "4:01 - 5:00", 8.50,5))
                (it.children).add(BetsAdapter.MyChildItem(5, "5:01+", 2.80,6))
            })

            for (i in 2..19) {
                val group = BetsAdapter.MyGroupItem(i.toLong(), "GROUP $i",sportsObject)
                for (j in 0..4) {
                    group.children as MutableList
                    group.children.add(BetsAdapter.MyChildItem(j.toLong(), "child $j", 0.00,j.toLong()))
                }
                this.add(group)
            }

        }
    }

}