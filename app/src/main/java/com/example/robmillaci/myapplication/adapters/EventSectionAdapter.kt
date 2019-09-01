package com.example.robmillaci.myapplication.adapters


import android.annotation.SuppressLint
import android.content.Context
import android.os.Build
import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.extension_functions.inflate
import com.example.robmillaci.myapplication.activities.fragments.home_activity.HomeActivityViewModel
import com.example.robmillaci.myapplication.miscs.CallingType
import com.example.robmillaci.myapplication.miscs.CustomLinearLayoutManager
import com.example.robmillaci.myapplication.miscs.SpacesItemDecoration
import com.example.robmillaci.myapplication.pojos.IEventObject
import java.lang.ref.WeakReference

/**
 * The adapter to populate the recyclerview in the home fragment with sports or racing related events
 * Each item within this adapter contains another recyclerview with the individual. This adapter displays the heading of each section
 * along with creating a sections recycler view to show the individual events (EventSectionAdapter)
 */
class EventAdapter(
    private val weakContext: WeakReference<Context>, private var itemList: MutableLiveData<ArrayList<SectionDataModel>>?
) : RecyclerView.Adapter<MyViewHolder>(), IadapterCallback {

    var mViewModel: HomeActivityViewModel? = null


    /**
     * On click for when a bet item / event item is clicked, informing the view model to update the bet slip
     */
    override fun betItemClicked(eventObject: IEventObject,position : Int , add: Boolean) {
        if (add) {
      //      mViewModel?.updateBetSlip(eventObject, position,true)
        } else {
      //      mViewModel?.updateBetSlip(eventObject, position,false)
        }
    }



    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        mViewModel = ViewModelProviders.of(weakContext.get() as FragmentActivity).get(HomeActivityViewModel::class.java)

    }



    @SuppressLint("InflateParams")
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(R.layout.sports_recyclerview.inflate(weakContext.get(), null))
    }


    /**
     * Sets the data to be using in this adapter. This is used to make changes between sports and racing data and also,
     * although not implemented, it would be used when observing the live sports data in the view model in order to capture changes to the data
     * if a retrofit call was made every X minutes to retrieve the latest data?
     */
    fun setData(itemList: ArrayList<SectionDataModel>?) {
        val newData = MutableLiveData<ArrayList<SectionDataModel>>()
        newData.value = itemList
        this.itemList = newData
    }


    override fun getItemCount(): Int {
        return itemList?.value?.size ?: 0
    }


    @SuppressLint("SetTextI18n")
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.title.text = this.itemList?.value?.get(position)?.headerTitle

        //Create the recyclerview contained within this event section
        val itemListDataAdapter: SectionListDataAdapter? =
            SectionListDataAdapter(weakContext, itemList?.value?.get(position)!!.allItemsInSection, this)

        itemListDataAdapter?.setHasStableIds(true)
        val layoutManager = CustomLinearLayoutManager(
            weakContext.get(),
            LinearLayoutManager.HORIZONTAL,
            false
        )

        holder.recyclerView.apply {
            this.layoutManager = layoutManager
            this.adapter = itemListDataAdapter
            this.isNestedScrollingEnabled = false

            if (this.itemDecorationCount == 0) {
                this.addItemDecoration(SpacesItemDecoration(10, CallingType.SPORT_CALLING_TYPE))
            }
        }
    }
}


class MyViewHolder(view: View) : RecyclerView.ViewHolder(view) {
    val title: TextView = view.findViewById(R.id.group_title)
    val recyclerView: RecyclerView = view.findViewById(R.id.children_recyclerView)
}


/**
 * A single Event object that forms part of the individual events within SectionDataModel
 */
class SingleItem {
    lateinit var eventObject: IEventObject
}

data class SectionDataModel(val headerTitle: String, val allItemsInSection: ArrayList<SingleItem>) {
    //The individual sections of data in the fragment - ie. Most popular, AFL, NRL etc. Each vertical section has a corresponding horizontal recyclerview containing the individual
    //data. This class has been implemented as an example JSON serializable class so we can convert any JSON data retrieved into a class object
    //Assumption that the JSON data is structured such that each response is a "SectionData" containing within it individual "SingleItem" objects
}