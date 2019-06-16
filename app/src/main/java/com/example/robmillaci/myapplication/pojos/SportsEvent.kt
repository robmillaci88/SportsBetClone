package com.example.robmillaci.myapplication.pojos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sports_objects")
class SportsEvent(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var specificType: SpecificTypes,
    val tournament: String,
    val gameName: String,
    private val startTime: String,
    private val endTime: String,
    private val mainEvent: Boolean,
    val homeOdds: Double, val awayOdds: Double,
    val type : String = "SportsEvent"
) : IEventObject {

    var chosenOutcomes = ""
    var chosenOdds = ""

    override fun getChosenOutcome(): String {
        return chosenOutcomes
    }

    override fun setChosenOutcome(team: String) {
        this.chosenOutcomes = team
    }

    override fun getchosenOdds(): String {
        return chosenOdds
    }

    override fun setchosenOdds(odds: String) {
        this.chosenOdds = odds
    }

    override fun isMainEvent(): Boolean {
        return mainEvent
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
        return EventType.SPORTS
    }


}
