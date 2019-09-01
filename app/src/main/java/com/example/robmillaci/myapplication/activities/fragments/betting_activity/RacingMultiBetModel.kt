package com.example.robmillaci.myapplication.activities.fragments.betting_activity
import com.example.robmillaci.myapplication.adapters.BetsAdapter
import com.example.robmillaci.myapplication.pojos.RacingEvent
import com.example.robmillaci.myapplication.pojos.SportsEvent

/**
 * A dummy model class that acts as though it is retrieving data from JSON/source system
 */
class RacingMultiBetModel {

    internal fun retrieveDataForObject(racingObject: RacingEvent): MutableList<BetsAdapter.MyGroupItem> {
        //RXJava calls to Json API / Database / file ...whatever the data source is to grab the data asynchronously
        //fake the data here ...
        return mutableListOf<BetsAdapter.MyGroupItem>().apply {
            this.add(BetsAdapter.MyGroupItem(0, "Winner",racingObject).also {
                (it.children as MutableList).add(BetsAdapter.MyChildItem(0, "Outinthestreet (4)", 3.00,1))
                (it.children).add(BetsAdapter.MyChildItem(1, "I'm Bulletproof (1)", 13.00,2))
                (it.children).add(BetsAdapter.MyChildItem(2, "Marman (16)", 56.00,3))
                (it.children).add(BetsAdapter.MyChildItem(3, "Amerissa (2)", 2.10,4))
                (it.children).add(BetsAdapter.MyChildItem(4, "Real Thinker (12)", 26.50,5))
                (it.children).add(BetsAdapter.MyChildItem(5, "Last one Named (9)", 4.50,6))
                (it.children).add(BetsAdapter.MyChildItem(6, "Waratah Girl (5)", 11.50,7))
                (it.children).add(BetsAdapter.MyChildItem(7, "Cosmic Charm (13)", 5.50,8))
                (it.children).add(BetsAdapter.MyChildItem(8, "Seldom Home (15)", 9.00,9))

            })

            this.add(BetsAdapter.MyGroupItem(1, "Top 2 Finisher",racingObject).also {
                (it.children as MutableList).add(BetsAdapter.MyChildItem(0, "Outinthestreet (4)", 7.00,1))
                (it.children).add(BetsAdapter.MyChildItem(9, "I'm Bulletproof (1)", 4.50,2))
                (it.children).add(BetsAdapter.MyChildItem(10, "Marman (16)", 5.50,3))
                (it.children).add(BetsAdapter.MyChildItem(11, "Amerissa (2)", 6.50,4))
                (it.children).add(BetsAdapter.MyChildItem(12, "Real Thinker (12)", 8.50,5))
                (it.children).add(BetsAdapter.MyChildItem(13, "Last one Named (9)", 4.50,6))
                (it.children).add(BetsAdapter.MyChildItem(14, "Waratah Girl (5)", 11.50,7))
                (it.children).add(BetsAdapter.MyChildItem(15, "Cosmic Charm (13)", 5.50,8))
                (it.children).add(BetsAdapter.MyChildItem(16, "Seldom Home (15)", 9.00,9))


            })

            this.add(BetsAdapter.MyGroupItem(2, "Top 3 Finisher",racingObject).also {
                (it.children as MutableList).add(BetsAdapter.MyChildItem(0, "Outinthestreet (4)", 7.00,1))
                (it.children).add(BetsAdapter.MyChildItem(17, "I'm Bulletproof (1)", 4.50,2))
                (it.children).add(BetsAdapter.MyChildItem(18, "Marman (16)", 5.50,3))
                (it.children).add(BetsAdapter.MyChildItem(19, "Amerissa (2)", 6.50,4))
                (it.children).add(BetsAdapter.MyChildItem(20, "Real Thinker (12)", 8.50,5))
                (it.children).add(BetsAdapter.MyChildItem(21, "Last one Named (9)", 4.50,6))
                (it.children).add(BetsAdapter.MyChildItem(22, "Waratah Girl (5)", 11.50,7))
                (it.children).add(BetsAdapter.MyChildItem(23, "Cosmic Charm (13)", 5.50,8))
                (it.children).add(BetsAdapter.MyChildItem(24, "Seldom Home (15)", 9.00,9))

            })

            this.add(BetsAdapter.MyGroupItem(3, "Top 4 Finisher",racingObject).also {
                (it.children as MutableList).add(BetsAdapter.MyChildItem(0, "Outinthestreet (4)", 7.00,1))
                (it.children).add(BetsAdapter.MyChildItem(25, "I'm Bulletproof (1)", 4.50,2))
                (it.children).add(BetsAdapter.MyChildItem(26, "Marman (16)", 5.50,3))
                (it.children).add(BetsAdapter.MyChildItem(27, "Amerissa (2)", 6.50,4))
                (it.children).add(BetsAdapter.MyChildItem(28, "Real Thinker (12)", 8.50,5))
                (it.children).add(BetsAdapter.MyChildItem(29, "Last one Named (9)", 4.50,6))
                (it.children).add(BetsAdapter.MyChildItem(30, "Waratah Girl (5)", 11.50,7))
                (it.children).add(BetsAdapter.MyChildItem(31, "Cosmic Charm (13)", 5.50,8))
                (it.children).add(BetsAdapter.MyChildItem(32, "Seldom Home (15)", 9.00,9))

            })

        }
    }

}