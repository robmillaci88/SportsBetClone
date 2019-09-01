package com.example.robmillaci.myapplication.activities.fragments.home_activity

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LifecycleObserver
import androidx.lifecycle.MutableLiveData
import com.example.robmillaci.myapplication.adapters.SectionDataModel
import com.example.robmillaci.myapplication.extension_functions.default
import com.example.robmillaci.myapplication.extension_functions.notifyObserver
import com.example.robmillaci.myapplication.pojos.IEventObject
import com.example.robmillaci.myapplication.retrofit.BetSportsAPI
import com.example.robmillaci.myapplication.retrofit.Retrofit
import io.reactivex.CompletableObserver
import io.reactivex.SingleObserver
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.Disposable
import io.reactivex.schedulers.Schedulers

/**
 * The view model for HomeActivityView
 */
class HomeActivityViewModel(application: Application) : AndroidViewModel(application), LifecycleObserver {
    var sectionDataSports: MutableLiveData<ArrayList<SectionDataModel>> =
        MutableLiveData() //Live data for sports events
    var sectionDataRacing: MutableLiveData<ArrayList<SectionDataModel>> =
        MutableLiveData() //Live data for racing events
    var betSlipItems: MutableLiveData<ArrayList<IEventObject>>? =
        MutableLiveData<ArrayList<IEventObject>>().default(ArrayList()) //Live data for events added to the bet slip
    var betSlipValue = MutableLiveData<Int>().default(0) //Live data for the number of events added to the betslip

//    private var database: AppDatabase? = createDatabase() //This apps Room database instance
//    private var sportsDao: SportsDAO? = null //sports room DAO
//    private var racingDao: RacingDAO? = null //racing room DAO
//

    private var dbUpdateObserver =
        object : CompletableObserver { //Observe database updates and update the betslip value on completion

            override fun onComplete() {
                betSlipValue.value = betSlipItems?.value?.size
            }

            override fun onSubscribe(d: Disposable) {
            }

            override fun onError(e: Throwable) {
                e.printStackTrace()
            }
        }


//    /**
//     * Returns all bets from the room database for both sports items and racing items
//     */
//    fun getAllBets(){
//        val combinedList : ArrayList<IEventObject>? = ArrayList()
//
//        val sportsObjectFromDB =sportsDao?.getAll()
//            ?.subscribeOn(Schedulers.io())
//            ?.observeOn(AndroidSchedulers.mainThread())
//            ?.subscribe { returnedSportsList, throwableSports ->
//                if(returnedSportsList != null){
//                    combinedList?.addAll(returnedSportsList)
//                }
//                racingDao?.getAll()?.subscribeOn(Schedulers.io())
//                    ?.subscribe{returnedRacingList, throwableRacing ->
//                        if(returnedRacingList != null){
//                            combinedList?.addAll(returnedRacingList)
//                        }
//                        betSlipItems?.postValue(combinedList)
//                    }
//            }
//
//    }

    /**
     * Called when we are updating the betslip with a new event object. This method updates the betSlipItems live data object as well as
     * adding/removing from the room database
     */
//    fun updateBetSlip(eventObject: IEventObject,position : Int, add: Boolean) {
//        if (add) {
//            this.betSlipItems?.value?.add(eventObject)
//            if (eventObject is SportsEvent) {
//                sportsDao?.insert(eventObject)
//                    ?.subscribeOn(Schedulers.io())
//                    ?.observeOn(AndroidSchedulers.mainThread())
//                    ?.subscribe(dbUpdateObserver)
//
//            } else if (eventObject is RacingEvent) {
//                racingDao?.insert(eventObject)
//                    ?.subscribeOn(Schedulers.io())
//                    ?.observeOn(AndroidSchedulers.mainThread())
//                    ?.subscribe(dbUpdateObserver)
//
//            }
//        } else {
//           this.betSlipItems?.value?.removeAt(position)
//            if (eventObject is SportsEvent) {
//                sportsDao?.delete(eventObject)
//                    ?.subscribeOn(Schedulers.io())
//                    ?.observeOn(AndroidSchedulers.mainThread())
//                    ?.subscribe(dbUpdateObserver)
//            } else if (eventObject is RacingEvent) {
//                racingDao?.delete(eventObject)
//                    ?.subscribeOn(Schedulers.io())
//                    ?.observeOn(AndroidSchedulers.mainThread())
//                    ?.subscribe(dbUpdateObserver)
//            }
//        }
//    }
//

//    /**
//     * Retrieves our Room database. AppDatabase (Room) is created as a Singleton
//     * Also created our DAO's
//     */
//    private fun createDatabase(): AppDatabase {
//        database = AppDatabase.getAppDataBase(getApplication<Application>().applicationContext)
//
//        sportsDao = database?.getSportsDao()
//        racingDao = database?.getRacingDao()
//
//        if (database!= null) {
//            return database!!
//        }else{
//            throw IllegalStateException("Error creating database")
//        }
//    }


    /**
     * Gets all our racing data via Retrofit which returns a Single response (similar to an observable)
     * We subscribe on Schedulers.io for asynch retrieval and observe on successful response on the main thread - updating the racing live data and notifying the observer
     * set in the home fragment to perform relevant UI changes
     */
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
                    e.printStackTrace()
                }
            })
    }


    /**
     * Gets all our sports data via Retrofit which returns a Single response (similar to an observable)
     * We subscribe on Schedulers.io for asynch retrieval and observe on successful response on the main thread - updating the sports live data
     * and notifying the observer set in the home fragment  to perform relevant UI changes
     */
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