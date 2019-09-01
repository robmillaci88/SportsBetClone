package com.example.robmillaci.myapplication.room

import android.content.Context
import androidx.room.*
import com.example.robmillaci.myapplication.pojos.RacingEvent
import com.example.robmillaci.myapplication.pojos.SportsEvent

/**
 * Our ROOM db implementation as a singleton. Includes all relevant DAO's
 */
//@Database(entities = [SportsEvent::class, RacingEvent::class], version = 1)
//@TypeConverters(TypeConverter::class)
//abstract class AppDatabase : RoomDatabase() {
//    abstract fun  getSportsDao(): SportsDAO
//    abstract fun  getRacingDao(): RacingDAO
//
//    companion object {
//        var INSTANCE: AppDatabase? = null
//
//        fun getAppDataBase(context: Context): AppDatabase? {
//            if (INSTANCE == null){
//                synchronized(AppDatabase::class){
//                    INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "myDB").build()
//                }
//            }
//            return INSTANCE
//        }
//
//        fun destroyDataBase(){
//            INSTANCE = null
//        }
//    }
//}