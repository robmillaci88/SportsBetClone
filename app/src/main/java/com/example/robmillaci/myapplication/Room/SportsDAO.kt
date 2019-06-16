package com.example.robmillaci.myapplication.Room

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.robmillaci.myapplication.pojos.IEventObject
import com.example.robmillaci.myapplication.pojos.SportsEvent
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single

@Dao
interface SportsDAO {
    @Insert
    fun insert(sportsObject: SportsEvent) : Completable

    @Delete
    fun delete(sportsObject: SportsEvent) : Completable

    @Query("SELECT * from sports_objects")
        fun getAll() : Single<List<SportsEvent>>

}