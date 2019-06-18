package com.example.robmillaci.myapplication.pojos

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

@Entity(tableName = "sports_objects")
class SportsEvent(
    @PrimaryKey(autoGenerate = true) var id: Int,
    var specificType: SpecificTypes,
    var tournament: String,
    var gameName: String,
    override var startTime: String,
    override var endTime: String,
    var mainEvent: Boolean,
    var homeOdds: Double, var awayOdds: Double,
    var type: String = "SportsEvent"
) : IEventObject {

    override var betName: String? = ""
    override var chosenOutcomes = ""
    override var chosenOdds = ""

    @Ignore
    override fun isMainEvent(): Boolean {
        return mainEvent
    }

    override fun getSpecificTypes(): SpecificTypes {
        return specificType
    }

    override fun getEventType(): EventType {
        return EventType.SPORTS
    }

}
