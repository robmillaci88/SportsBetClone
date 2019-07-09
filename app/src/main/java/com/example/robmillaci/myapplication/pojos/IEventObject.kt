package com.example.robmillaci.myapplication.pojos

import java.io.Serializable

/**
 * Interface for creating of events (Sports events and racing events)
 */
interface IEventObject : ISpecificTypes, Serializable {
    fun getEventType(): EventType
    fun isMainEvent(): Boolean
    var betName: String?
    var chosenOutcomes: String
    var chosenOdds: String
    var startTime : String
    var endTime : String
}