package com.example.robmillaci.myapplication.pojos

import com.google.gson.annotations.SerializedName

enum class SpecificTypes {
    @SerializedName("RUGBY")
    RUGBY{
        override fun toString(): String {
            return "Rubgy"
        }
    }
    ,
    @SerializedName("CRICKET")
    CRICKET{
        override fun toString(): String {
            return "Cricket"
        }
    }
    ,
    @SerializedName("TENNIS")
    TENNIS{
        override fun toString(): String {
            return "Tennis"
        }
    }
    ,
    @SerializedName("SOCCER")
    SOCCER{
        override fun toString(): String {
            return "Soccer"
        }
    }
    ,
    @SerializedName("AFL")
    AFL{
        override fun toString(): String {
            return "AFL"
        }
    }
    ,
    @SerializedName("NRL")
    NRL{
        override fun toString(): String {
            return "NRL"
        }
    }
    ,

    @SerializedName("HORSES")
    HORSES {
        override fun toString(): String {
            return "Horses"
        }
    }
    ,

    @SerializedName("GREYHOUND")
    GREYHOUND {
        override fun toString(): String {
            return "Greyhound"
        }
    }
    ,
    @SerializedName("HARNESS")
    HARNESS {
        override fun toString(): String {
            return "Harness"
        }
    }


}