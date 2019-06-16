package com.example.robmillaci.myapplication.pojos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "racing_objects")
class RacingEvent(
    @PrimaryKey(autoGenerate = true) var id : Int,
    var specificType: SpecificTypes,
    val raceLocation: String,
    val raceName: String,
    private val startTime: String,
    private val endTime: String,
    val type : String = "RacingEvent"
) : IEventObject {

    var chosenOutcomes = ""
    var chosenOdds = ""

    override fun getchosenOdds(): String {
        return chosenOdds
    }

    override fun setchosenOdds(odds: String) {
        this.chosenOdds = odds
    }


    override fun getChosenOutcome(): String {
        return chosenOutcomes
    }

    override fun setChosenOutcome(team : String) {
       this.chosenOutcomes = team
    }

    override fun isMainEvent(): Boolean {
        return false
    }

    override fun getEndTime(): String {
        return endTime
    }

    override fun getStartTime(): String {
        return startTime
    }

    override fun getSpecificTypes(): SpecificTypes {
        return specificType
    }

    override fun getEventType(): EventType {
        return EventType.RACING
    }



}