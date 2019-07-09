package com.example.robmillaci.myapplication.adapters

import android.content.Context
import android.view.ViewGroup
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.extension_functions.inflate
import com.example.robmillaci.myapplication.pojos.EventType
import java.lang.ref.WeakReference

/**
 * Helper class for building the viewholder type for SectionListDataAdapter
 */
class ViewHolderBuilder(private val weakContext : WeakReference<Context>, val parent : ViewGroup) {
    fun get(isMainEvent : Boolean, eventType : EventType) : SectionListDataAdapter.MyViewHolder {
        return when {
            isMainEvent -> {
                SectionListDataAdapter.MyViewHolderMainEvent(
                    R.layout.main_event_card_view.inflate(
                        weakContext.get()!!,
                        parent
                    )
                )
            }

            eventType == EventType.RACING -> SectionListDataAdapter.MyViewHolderRace(
                R.layout.racing_recycler_view_item.inflate(
                    weakContext.get()!!,
                    null
                )
            )

            eventType == EventType.SPORTS -> SectionListDataAdapter.MyViewHolderSports(
                R.layout.sports_recycler_view_item.inflate(
                    weakContext.get()!!,
                    null
                )
            )

            else -> SectionListDataAdapter.MyViewHolderSports(
                R.layout.sports_recycler_view_item.inflate(
                    weakContext.get()!!,
                    null
                )
            )
        }
    }
}