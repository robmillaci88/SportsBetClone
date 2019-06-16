package com.example.robmillaci.myapplication.pojos

import androidx.room.Entity
import java.io.Serializable



interface IEventObject : ISpecificTypes, Serializable {
    fun getEventType() : EventType
    fun isMainEvent() : Boolean
    fun getStartTime() : String
    fun getEndTime() : String
    fun getchosenOdds() : String
    fun setchosenOdds(odds : String)
    fun getChosenOutcome() : String
    fun setChosenOutcome(team : String)
}