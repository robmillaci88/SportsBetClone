package com.example.robmillaci.myapplication.room


import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.robmillaci.myapplication.pojos.RacingEvent
import io.reactivex.Completable
import io.reactivex.Single

@Dao
interface RacingDAO {
    @Insert
    fun insert(sportsObject: RacingEvent) : Completable

    @Delete
    fun delete(sportsObject: RacingEvent) : Completable

    @Query("SELECT * from racing_objects")
    fun getAll() : Single<List<RacingEvent>>

}