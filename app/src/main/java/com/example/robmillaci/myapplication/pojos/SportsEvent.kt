package com.example.robmillaci.myapplication.pojos

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey

/**
 * Class responsible for creating Sports event objects. Also an entity within out Room database implementation
 */
@Entity(tableName = "sports_objects")
class SportsEvent(
    @PrimaryKey(autoGenerate = true) var id: Int,//the primary key used to store in Room DB
    var specificType: SpecificTypes,//the specific type of sports event (i.e Rugby, Tennis, AFL etc)
    var tournament: String, //The sports tournament name
    var gameName: String, //The specific game name
    override var startTime: String, //the start time of the event
    override var endTime: String, //the end time of the event
    var mainEvent: Boolean, //is this a main event?
    var homeOdds: Double, var awayOdds: Double, // home and away odds
    var type: String = "SportsEvent" //The type used for successfully parsing the JSON data with our custom interface adapter
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
