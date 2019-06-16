package com.example.robmillaci.myapplication.activities.fragments.home_activity

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.example.robmillaci.myapplication.Room.AppDatabase
import com.example.robmillaci.myapplication.Room.RacingDAO
import com.example.robmillaci.myapplication.Room.SportsDAO
import com.example.robmillaci.myapplication.adapters.SectionDataModel
import com.example.robmillaci.myapplication.extension_functions.default
import com.example.robmillaci.myapplication.extension_functions.notifyObserver
import com.example.robmillaci.myapplication.pojos.IEventObject
import com.example.robmillaci.myapplication.pojos.RacingEvent
import com.example.robmillaci.myapplication.pojos.SportsEvent
import com.example.robmillaci.myapplication.retrofit.BetSportsAPI
import com.example.robmillaci.myapplication.retrofit.Retrofit
import io.reactivex.*
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers
import java.lang.IllegalStateException

class HomeActivityViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    var sectionDataSports: MutableLiveData<ArrayList<SectionDataModel>> = MutableLiveData()
    var sectionDataRacing: MutableLiveData<ArrayList<SectionDataModel>> = MutableLiveData()
    var betSlipItems: MutableLiveData<ArrayList<IEventObject>>? = MutableLiveData<ArrayList<IEventObject>>().default(ArrayList())
    var betSlipValue = MutableLiveData<Int>().default(0)

    private var database: AppDatabase? = createDatabase()
    private var sportsDao: SportsDAO? = null
    private var racingDao: RacingDAO? = null


    private  var dbUpdateObserver  =  object : CompletableObserver {
        override fun onComplete() {
            Log.d("OBSERVING","DB UPDATE OBSERVER CALLED")
            betSlipValue.value = betSlipItems?.value?.size
        }
        override fun onSubscribe(d: Disposable) {
        }
        override fun onError(e: Throwable) {
            Log.d("OBSERVING","DB UPDATE OBSERVER ERROR ${e.localizedMessage}")
        }
    }


    fun getAllBets(){
        val combinedList : ArrayList<IEventObject>? = ArrayList()

        val sportsObjectFromDB =sportsDao?.getAll()
            ?.subscribeOn(Schedulers.io())
            ?.observeOn(AndroidSchedulers.mainThread())
            ?.subscribe { returnedSportsList, throwableSports ->
                if(returnedSportsList != null){
                    combinedList?.addAll(returnedSportsList)
                }
                racingDao?.getAll()?.subscribeOn(Schedulers.io())
                    ?.subscribe{returnedRacingList, throwableRacing ->
                        if(returnedRacingList != null){
                            combinedList?.addAll(returnedRacingList)
                        }
                        betSlipItems?.postValue(combinedList)
                    }
            }

    }

    fun updateBetSlip(eventObject: IEventObject,position : Int, add: Boolean) {
        if (add) {
            this.betSlipItems?.value?.add(eventObject)
            if (eventObject is SportsEvent) {
                sportsDao?.insert(eventObject)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(dbUpdateObserver)

            } else if (eventObject is RacingEvent) {
                racingDao?.insert(eventObject)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(dbUpdateObserver)

            }
        } else {
           this.betSlipItems?.value?.removeAt(position)
            if (eventObject is SportsEvent) {
                sportsDao?.delete(eventObject)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(dbUpdateObserver)
            } else if (eventObject is RacingEvent) {
                racingDao?.delete(eventObject)
                    ?.subscribeOn(Schedulers.io())
                    ?.observeOn(AndroidSchedulers.mainThread())
                    ?.subscribe(dbUpdateObserver)
            }
        }
    }


    fun createDatabase(): AppDatabase {
        database = AppDatabase.getAppDataBase(getApplication<Application>().applicationContext)

        sportsDao = database?.getSportsDao()
        racingDao = database?.getRacingDao()

        if (database!= null) {
            return database!!
        }else{
            throw IllegalStateException("Error creating database")
        }
    }


    fun getRacingData() {
        val callClient = Retrofit.Init.retrofitInstance.create(BetSportsAPI::class.java)
        callClient.getRacingData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<ArrayList<SectionDataModel>> {
                override fun onSuccess(returnedData: ArrayList<SectionDataModel>) {
                    sectionDataRacing.value = returnedData
                    sectionDataRacing.notifyObserver()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    Log.d("GETRACGINDATA","data is errored ${e.printStackTrace()}")

                }
            })
    }


    fun getSportsData() {
        val callClient = Retrofit.Init.retrofitInstance.create(BetSportsAPI::class.java)
        callClient.getSportsData()
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribe(object : SingleObserver<ArrayList<SectionDataModel>> {
                override fun onSuccess(returnedData: ArrayList<SectionDataModel>) {
                    sectionDataSports.value = returnedData
                    sectionDataSports.notifyObserver()
                }

                override fun onSubscribe(d: Disposable) {
                }

                override fun onError(e: Throwable) {
                    //do error handling stuff
                }
            })
    }


}