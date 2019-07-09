package com.example.robmillaci.myapplication.pojos

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Class responsible for creating Racing event objects. Also an entity within out Room database implementation
 */
@Entity(tableName = "racing_objects")
class RacingEvent(
    @PrimaryKey(autoGenerate = true) var id : Int, //the primary key used to store in Room DB
    var specificType: SpecificTypes, //the specific type of racing event (i.e Harness, Greys etc)
    var raceLocation: String, //The location of the racing event
    var raceName: String, //the race name
    override var startTime: String, //the start time of the event
    override var endTime: String, //the end time of the event
    var type : String = "RacingEvent" //The type used for successfully parsing the JSON data with our custom interface adapter
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