package com.example.robmillaci.myapplication.adapters

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.extension_functions.determineImageDrawable
import com.example.robmillaci.myapplication.extension_functions.inflate
import com.example.robmillaci.myapplication.activities.fragments.home_activity.HomeActivityViewModel
import com.example.robmillaci.myapplication.pojos.IEventObject
import com.example.robmillaci.myapplication.pojos.SportsEvent
import java.lang.ref.WeakReference

class BetSlipAdapter(val weakContext: WeakReference<Context>, val items: ArrayList<IEventObject>?) :
    RecyclerView.Adapter<BetSlipAdapter.MyBetSlipViewHolder>() {
    var mViewModel: HomeActivityViewModel =
        ViewModelProviders.of(weakContext.get() as FragmentActivity).get(HomeActivityViewModel::class.java)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBetSlipViewHolder {
        return MyBetSlipViewHolder(
            R.layout.bet_slip_event.inflate(
                weakContext.get(),
                parent
            )
        )
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyBetSlipViewHolder, position: Int) {
        val eventObject = items?.get(position)
        if (eventObject is SportsEvent) {
            holder.bet_icon.setImageDrawable(
                holder.bet_icon.determineImageDrawable(
                    weakContext,
                    eventObject.getSpecificTypes()
                )
            )
            holder.bet_type.text = weakContext.get()?.getString(R.string.win_or_place_string)
            holder.eventName.text = eventObject.gameName
            holder.betOdds.text = eventObject.chosenOdds
            holder.bet_chosen_team.text = eventObject.getChosenOutcome()
            holder.entryNumber.text = holder.adapterPosition.toString()

            holder.deleteBet.setOnClickListener {
                val itemToRemove = items?.get(holder.adapterPosition)
                if (itemToRemove != null) {
                    mViewModel.updateBetSlip(itemToRemove, holder.adapterPosition, false)
                    notifyItemRemoved(holder.adapterPosition);
                }
            }
        }
    }

    class MyBetSlipViewHolder(v: View) : RecyclerView.ViewHolder(v) {
        val bet_icon = v.findViewById<ImageView>(R.id.betslip_entry_icon)
        val bet_type: TextView = v.findViewById(R.id.bet_type)
        val bet_chosen_team: TextView = v.findViewById(R.id.bet_chosen_team)
        val eventName: TextView = v.findViewById(R.id.event_name)
        val entryNumber: TextView = v.findViewById(R.id.entry_number)
        val betOdds: TextView = v.findViewById(R.id.bet_odds)
        val potentialReturns: TextView = v.findViewById(R.id.potential_returns)
        val deleteBet: ImageView = v.findViewById(R.id.delete_bet)
    }
}

