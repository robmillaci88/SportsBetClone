@file:Suppress("DEPRECATION")

package com.example.robmillaci.myapplication.adapters


import android.content.Context
import android.content.Intent
import android.os.Build
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.core.view.children
import androidx.recyclerview.widget.RecyclerView
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.activities.fragments.betting_activity.SportsBettingActivityView
import com.example.robmillaci.myapplication.extension_functions.determineImageDrawable
import com.example.robmillaci.myapplication.miscs.Timer
import com.example.robmillaci.myapplication.miscs.gimmeDateTime
import com.example.robmillaci.myapplication.pojos.EventType
import com.example.robmillaci.myapplication.pojos.IEventObject
import com.example.robmillaci.myapplication.pojos.RacingEvent
import com.example.robmillaci.myapplication.pojos.SportsEvent
import kotlinx.android.synthetic.main.main_event_card_view.view.*
import kotlinx.android.synthetic.main.racing_recycler_view_item.view.*
import kotlinx.android.synthetic.main.sports_recycler_view_item.view.*
import kotlinx.android.synthetic.main.sports_recycler_view_item.view.date_time
import kotlinx.android.synthetic.main.sports_recycler_view_item.view.icon
import kotlinx.android.synthetic.main.sports_recycler_view_item.view.time_to_go
import java.lang.ref.WeakReference
import java.time.Duration
import java.time.LocalDateTime

class SectionListDataAdapter(
    val weakContext: WeakReference<Context>, var itemList: ArrayList<SingleItem>, val adapterCallback: IadapterCallback
) : RecyclerView.Adapter<SectionListDataAdapter.MyViewHolder>(), ItimerCommunication {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val dataType = itemList[0].eventObject.getEventType()
        val mainEvent = itemList[0].eventObject.isMainEvent()

        return ViewHolderBuilder(weakContext, parent).get(mainEvent, dataType)

    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = itemList[position].eventObject

        /**
         * Logic to update the viewholders views depending on the Event type
         */
        when {
            event.getEventType() == EventType.SPORTS && !event.isMainEvent() -> {
                val sportsObject = event as SportsEvent
                holder as MyViewHolderSports
                holder.tournament?.text = sportsObject.tournament
                holder.game?.text = sportsObject.gameName
                holder.dateTime?.text = sportsObject.startTime
            }

            event.getEventType() == EventType.SPORTS && event.isMainEvent() -> {
                val mainEventObject = event as SportsEvent
                holder as MyViewHolderMainEvent
                holder.homeButtonText?.text = mainEventObject.gameName.split(" v ")[0]
                holder.awayButtonText?.text = mainEventObject.gameName.split(" v ")[1]

                holder.mainEventButtonOne?.setOnClickListener { mainEventOnClick(it, mainEventObject, position) }
                holder.mainEventButtonTwo?.setOnClickListener { mainEventOnClick(it, mainEventObject, position) }

                holder.mainEventGame?.text = mainEventObject.gameName
                holder.homeOdds?.text = mainEventObject.homeOdds.toString()
                holder.awayOdds?.text = mainEventObject.awayOdds.toString()
            }

            event.getEventType() == EventType.RACING -> {
                val racingObject = event as RacingEvent
                holder as MyViewHolderRace
                holder.place?.text = racingObject.raceLocation
                holder.race?.text = racingObject.raceName
                holder.dateTime?.text = racingObject.startTime
            }
        }

        //Determine which icon the viewholder should display
        val iconDrawable = holder.icon?.determineImageDrawable(weakContext, event.getSpecificTypes())
        holder.icon?.setImageDrawable(iconDrawable)

        //Determine the time before an event starts, the start time and the end time of an event
        val diff = Duration.between(LocalDateTime.now(), event.startTime.gimmeDateTime())
        val startTime = event.startTime.gimmeDateTime()
        val endTime = event.endTime.gimmeDateTime()


        //Logic for displaying specific timing related UI changes such as 'live now' badge and the timer background
        if (startTime.dayOfYear <= LocalDateTime.now().dayOfYear + 1 && startTime.hour - LocalDateTime.now().hour <= 24) {
            when {
                LocalDateTime.now() < endTime
                        && LocalDateTime.now() > startTime -> holder.timer?.setBackgroundResource(
                    R.drawable.live_icon
                )
                LocalDateTime.now() > endTime -> holder.timer?.visibility = View.GONE
                else -> Timer(this).createTimer(holder.timer, holder.adapterPosition, diff.toMillis())
            }
        } else {
            holder.timer?.visibility = View.GONE
        }


        //The on click event of each individual event object, to start the Sports betting activity for that specific event
        holder.itemView.setOnClickListener {
            if (event.getEventType() == EventType.SPORTS) {
                val bettingActivityIntent = Intent(
                    weakContext.get(),
                    SportsBettingActivityView::class.java
                )
                bettingActivityIntent.putExtra("object", event)
                weakContext.get()?.startActivity(bettingActivityIntent)
            }
        }
    }


    //Separated on click for a 'Main event' in order to implement the button click effects (blue ripple) as well as determining
    //the chosen odds and chosen outcomes
    private fun mainEventOnClick(view: View, eventObject: SportsEvent, position: Int) {
        if (view.tag == weakContext.get()?.getString(R.string.unclicked_button)) {
            view.setBackgroundResource(R.drawable.bet_btn_pressed)
            view.tag = weakContext.get()?.getString(R.string.clicked_button)
            (view as ViewGroup).children.forEach {
                if (it is TextView) {
                    it.setTextColor(weakContext.get()?.resources?.getColor(android.R.color.white)!!)
                    if (it.id == R.id.home_odds || it.id == R.id.away_odds) {
                        eventObject.chosenOdds = it.text.toString()
                    }
                    if (it.id == R.id.home_button_txt || it.id == R.id.away_button_txt) {
                        eventObject.chosenOutcomes = it.text.toString()
                    }
                }
            }
            eventObject.betName = "Head to head"
            adapterCallback.betItemClicked(eventObject, position, true)
        } else {
            view.setBackgroundResource(R.drawable.bet_btn_unpressed)
            view.tag = weakContext.get()?.getString(R.string.unclicked_button)
            (view as ViewGroup).children.forEach {
                if (it is TextView) {
                    it.setTextColor(weakContext.get()?.resources?.getColor(android.R.color.black)!!)
                    if (it.id == R.id.home_odds || it.id == R.id.away_odds) {
                        eventObject.chosenOdds = ""
                    }
                    if (it.id == R.id.home_button_txt || it.id == R.id.away_button_txt) {
                        eventObject.chosenOutcomes = ""
                    }
                }
            }
        }
    }


    override fun getItemCount(): Int {
        return itemList.size
    }

    override fun getItemId(position: Int): Long {
        return itemList[position].hashCode().toLong() + System.currentTimeMillis()
    }


    /*
    The individual view holders for sports events, racings events and main events
     */
    open class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val dateTime: TextView? = view.date_time
        val icon: ImageView? = view.icon
        val timer: TextView? = view.time_to_go
    }

    open class MyViewHolderSports(view: View) : MyViewHolder(view) {
        val game: TextView? = view.game
        val tournament: TextView? = view.tournament
    }

    class MyViewHolderRace(view: View) : MyViewHolder(view) {
        val place: TextView? = view.race_place
        val race: TextView? = view.race
    }

    class MyViewHolderMainEvent(view: View) : MyViewHolderSports(view) {
        val mainEventButtonOne: LinearLayout? = view.main_event_btn
        val mainEventButtonTwo: LinearLayout? = view.main_event_btn2
        val mainEventGame: TextView? = view.main_event_game
        val homeButtonText: TextView? = view.home_button_txt
        val awayButtonText: TextView? = view.away_button_txt
        val homeOdds: TextView? = view.home_odds
        val awayOdds: TextView? = view.away_odds
    }


    /**
     * Method to remove an event once the time has run out and update the recyclerview
     */
    override fun removeItemOverTime(position: Int) {
        itemList.removeAt(position)
        notifyItemChanged(position)
    }

}

interface ItimerCommunication {
    fun removeItemOverTime(position: Int)
}

interface IadapterCallback {
    fun betItemClicked(eventObject: IEventObject, position: Int, add: Boolean)
}

