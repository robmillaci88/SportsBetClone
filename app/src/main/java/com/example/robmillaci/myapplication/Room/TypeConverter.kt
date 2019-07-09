package com.example.robmillaci.myapplication.Room

import androidx.room.TypeConverter
import com.example.robmillaci.myapplication.pojos.IEventObject
import com.example.robmillaci.myapplication.pojos.SpecificTypes
import com.example.robmillaci.myapplication.retrofit.Retrofit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

/**
 * Room additional type converters
 */
class TypeConverter {
        @TypeConverter
        fun fromString(jsonString: String): ArrayList<IEventObject> {
            val type = object : TypeToken<ArrayList<IEventObject>>() {}.type
            return Gson().fromJson(jsonString, type)
        }

        @TypeConverter
        fun fromArrayList(objects: ArrayList<IEventObject>): String {
            val type = object : TypeToken<ArrayList<IEventObject>>() {}.type
            val gson = Retrofit.CustomGson.init()
            return gson.toJson(objects, type)
        }

        @TypeConverter
        fun fromRaceType(type: SpecificTypes?): String {
            if (type == null) {
                return ""
            } else {
                return type.toString()
            }
        }

        @TypeConverter
        fun fromStringToType(typeString: String): SpecificTypes? {
            return when (typeString) {
                "Rubgy" -> SpecificTypes.RUGBY
                "Cricket" -> SpecificTypes.CRICKET
                "Tennis" -> SpecificTypes.TENNIS
                "Soccer" -> SpecificTypes.SOCCER
                "AFL" -> SpecificTypes.AFL
                "NRL" -> SpecificTypes.NRL
                "Horses" -> SpecificTypes.HORSES
                "Harness" -> SpecificTypes.HARNESS
                else -> SpecificTypes.HORSES

            }
        }
}