package com.example.robmillaci.myapplication.extension_functions

import com.example.robmillaci.myapplication.pojos.IEventObject

fun <IEventObject> List<IEventObject>.toArrayList() : ArrayList<IEventObject>{
    val newArray : ArrayList<IEventObject> = ArrayList()
    for (e in this){
        newArray.add(e)
    }
    return newArray
}