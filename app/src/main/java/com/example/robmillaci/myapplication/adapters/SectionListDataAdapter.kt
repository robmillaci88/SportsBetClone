package com.example.robmillaci.myapplication.adapters


import android.annotation.SuppressLint
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


    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val dataType = itemList[0].eventObject.getEventType()
        val mainEvent = itemList[0].eventObject.isMainEvent()

        return ViewHolderBuilder(weakContext, parent).get(mainEvent, dataType)

    }


    @Suppress("DEPRECATION")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val event = itemList[position].eventObject

        when {
            event.getEventType() == EventType.SPORTS && !event.isMainEvent() -> {
                val sportsObject = event as SportsEvent
                holder as MyViewHolderSports
                holder.tournament?.text = sportsObject.tournament
                holder.game?.text = sportsObject.gameName
                holder.dateTime?.text = sportsObject.getStartTime()
            }

            event.getEventType() == EventType.SPORTS && event.isMainEvent() -> {
                val mainEventObject = event as SportsEvent
                holder as MyViewHolderMainEvent
                holder.homeButtonText?.text = mainEventObject.gameName.split(" v ")[0]
                holder.awayButtonText?.text = mainEventObject.gameName.split(" v ")[1]

                holder.mainEventButtonOne?.setOnClickListener { mainEventOnClick(it, mainEventObject,position) }
                holder.mainEventButtonTwo?.setOnClickListener { mainEventOnClick(it, mainEventObject,position) }


                holder.mainEventGame?.text = mainEventObject.gameName
                holder.homeOdds?.text = mainEventObject.homeOdds.toString()
                holder.awayOdds?.text = mainEventObject.awayOdds.toString()
            }

            event.getEventType() == EventType.RACING -> {
                val racingObject = event as RacingEvent
                holder as MyViewHolderRace
                holder.place?.text = racingObject.raceLocation
                holder.race?.text = racingObject.raceName
                holder.dateTime?.text = racingObject.getStartTime()
            }
        }

        val iconDrawable = holder.icon?.determineImageDrawable(weakContext, event.getSpecificTypes())
        holder.icon?.setImageDrawable(iconDrawable)

        val diff = Duration.between(LocalDateTime.now(), event.getStartTime().gimmeDateTime())
        val startTime = event.getStartTime().gimmeDateTime()
        val endTime = event.getEndTime().gimmeDateTime()

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

        holder.itemView.setOnClickListener {
            val bettingActivityIntent = Intent(
                weakContext.get(),
                SportsBettingActivityView::class.java
            )
            bettingActivityIntent.putExtra("object", event)
            weakContext.get()?.startActivity(bettingActivityIntent)
        }
    }


    private fun mainEventOnClick(view: View, eventObject: SportsEvent, position : Int) {
        if (view.tag == "unClicked") {
            view.setBackgroundResource(R.drawable.ripple_button_blue_pressed)
            view.tag = "clicked"
            (view as ViewGroup).children.forEach {
                if (it is TextView) {
                    it.setTextColor(weakContext.get()?.resources?.getColor(android.R.color.white)!!)
                    if (it.id == R.id.home_odds || it.id == R.id.away_odds) {
                        eventObject.chosenOdds = it.text.toString()
                    }
                    if (it.id == R.id.home_button_txt || it.id == R.id.away_button_txt) {
                        eventObject.setChosenOutcome(it.text.toString())
                    }
                }
            }
            adapterCallback.betItemClicked(eventObject,position, true)
        } else {
            view.setBackgroundResource(R.drawable.ripple_button_blue)
            view.tag = "unClicked"
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

    override fun removeItemOverTime(position: Int) {
        itemList.removeAt(position)
        notifyItemChanged(position)
    }

}

interface ItimerCommunication {
    fun removeItemOverTime(position: Int)
}

interface IadapterCallback {
    fun betItemClicked(eventObject: IEventObject, position : Int , add: Boolean)
}

