package com.example.robmillaci.myapplication.activities.fragments.betting_activity

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.robmillaci.myapplication.adapters.BetsAdapter
import com.example.robmillaci.myapplication.pojos.RacingEvent

class RacingMultiBetViewModel : ViewModel() {
    var mNumberOfMultiBets: MutableLiveData<Int> = MutableLiveData()
    private val mModel: RacingMultiBetModel = RacingMultiBetModel()
    //retrieve the data from the model related to the specific sports object clicked - returning all relevant betting data

    internal fun getDummyData(racingObject: RacingEvent): MutableList<BetsAdapter.MyGroupItem> {
        //this data is faked ...obviously :)
//        val callClient = Retrofit.Init.retrofitInstance.create(BetSportsAPI::class.java)
//        callClient.getSpecificRaceData(racingObject.raceId)
//            .subscribeOn(Schedulers.io())
//            .observeOn(AndroidSchedulers.mainThread())
//            .subscribe(object : SingleObserver<ArrayList<SectionDataModel>> {
//                override fun onSuccess(returnedData: ArrayList<SectionDataModel>) {
//                    mRaceData.value = returnedData
//                    //sectionDataSports.notifyObserver()
//                }
//
//                override fun onSubscribe(d: Disposable) {
//                }
//
//                override fun onError(e: Throwable) {
//                    //do error handling stuff
//                }
//            })
        return mModel.retrieveDataForObject(racingObject)
    }
}

interface repos {
    fun <T> onSuccess( results : List<T>)
    fun onError(e : Exception)
}