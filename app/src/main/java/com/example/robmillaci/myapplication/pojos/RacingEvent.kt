package com.example.robmillaci.myapplication.pojos

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "racing_objects")
class RacingEvent(
    @PrimaryKey(autoGenerate = true) var id : Int,
    var specificType: SpecificTypes,
    var raceLocation: String,
    var raceName: String,
    override var startTime: String,
    override var endTime: String,
    var type : String = "RacingEvent"
) : IEventObject {


    override var betName: String? = ""
    override  var chosenOutcomes = ""
    override var chosenOdds = ""

    override fun isMainEvent(): Boolean {
        return false
    }

    override fun getSpecificTypes(): SpecificTypes {
        return specificType
    }

    override fun getEventType(): EventType {
        return EventType.RACING
    }

}