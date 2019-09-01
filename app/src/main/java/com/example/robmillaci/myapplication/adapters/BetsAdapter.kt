package com.example.robmillaci.myapplication.adapters

import android.util.Log
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.lifecycle.ViewModel
import androidx.recyclerview.widget.RecyclerView
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.activities.fragments.betting_activity.RacingMultiBetViewModel
import com.example.robmillaci.myapplication.extension_functions.inflate
import com.example.robmillaci.myapplication.miscs.rotateTheView
import com.example.robmillaci.myapplication.pojos.IEventObject
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder
import kotlinx.android.synthetic.main.bet_type_children_view.view.*
import kotlinx.android.synthetic.main.bet_type_recycler_view.view.*

/**
 * The adapter for the different types of bets that can be chosen within the Sports betting activity
 * uses 3rd party library sectionedrecyclerviewadapter
 */
internal class BetsAdapter(
    groupItems: List<MyGroupItem>, private val itemDecoration: RecyclerView.ItemDecoration,
    private val viewBetClickedListener: IViewbetClickedListener
) :
    AbstractExpandableItemAdapter<BetsAdapter.MyGroupViewHolder, BetsAdapter.MyChildViewHolder>() {

    private var mItems: List<MyGroupItem>? = groupItems
    private var recyclerView: RecyclerView? = null
    private var parentScenarioChosen: Long? = null
    private var childChosenView: View? = null
    private var childChosenId: Long? = null
    private lateinit var mViewModel : RacingMultiBetViewModel

    init {
        setHasStableIds(true)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): MyGroupViewHolder {
        return MyGroupViewHolder(R.layout.bet_type_recycler_view.inflate(parent?.context!!, parent))
    }

    fun setViewModel(viewModel : RacingMultiBetViewModel){
        mViewModel = viewModel
    }


    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): MyChildViewHolder {
        return MyChildViewHolder(R.layout.bet_type_children_view.inflate(parent?.context!!, parent))
    }

    override fun onBindGroupViewHolder(
        holder: MyGroupViewHolder,
        groupPosition: Int,
        viewType: Int
    ) {
        val group = mItems?.get(groupPosition)
        holder.textView.text = group?.text
    }

    override fun onBindChildViewHolder(
        holder: MyChildViewHolder,
        groupPosition: Int,
        childPosition: Int,
        viewType: Int
    ) {
        val parent = mItems?.get(groupPosition)
        val child = parent?.children!![childPosition]

        holder.textView.text = child.text
        holder.oddsTv.text = child.odds.toString()


        //todo handle scenario where participant ID (a horse) is clicked in more than one place

        holder.bet_button.setOnClickListener {
            val thisEventObject = parent.thisEvent
            if (parentScenarioChosen == parent.id) { //if we are choosing a bet within the same parent event as prev chosen
                if (child.id == childChosenId) { //user is clicking on the same bet again ... remove it
                    removeTheBet(parent.id) //remove the old bet
                } else { //we are in the same parent, but clicking on a new bet
                    removeTheBet(parent.id) //remove the old bet
                    addTheNewBet(holder, child, parent, thisEventObject) //add the new one
                }
            } else {
                addTheNewBet(holder, child, parent, thisEventObject)
            }
        }
    }


    private fun addTheNewBet(
        holder: MyChildViewHolder,
        child: MyChildItem,
        parent: MyGroupItem,
        thisEventObject: IEventObject
    ) {
        childChosenView = holder.bet_button
        childChosenId = child.id
        viewBetClickedListener.betClicked(holder.bet_button, thisEventObject, parent.text) //add the new one
        parentScenarioChosen = parent.id//keep track of the current parent scenarios chosen by the user (one per event)
        mViewModel.mNumberOfMultiBets.value?.plus(1)
    }

    private fun removeTheBet(parentId : Long) {
        viewBetClickedListener.removeTheBet(childChosenView?.bet_button as View)
        parentScenarioChosen = null
        childChosenView = null
        mViewModel.mNumberOfMultiBets.value?.minus(1)
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return mItems?.get(groupPosition)?.children?.get(childPosition)?.id!!
    }

    override fun getGroupId(groupPosition: Int): Long {
        return mItems?.get(groupPosition)?.id!!
    }

    override fun getChildCount(groupPosition: Int): Int {
        return mItems?.get(groupPosition)?.children?.size ?: 0
    }

    override fun getGroupCount(): Int {
        return mItems?.size ?: 0
    }

    override fun onCheckCanExpandOrCollapseGroup(
        holder: MyGroupViewHolder,
        groupPosition: Int,
        x: Int,
        y: Int,
        expand: Boolean
    ): Boolean {

//        if (expand) {
//            recyclerView?.removeItemDecoration(itemDecoration)
//        } else {
//            recyclerView?.addItemDecoration(itemDecoration)
//        }
        return true
    }

    interface IViewbetClickedListener {
        fun betClicked(view: View, eventObject: IEventObject, betName: String)
        fun removeTheBet(view: View)
    }

    internal abstract class MyBaseItem(val id: Long, val text: String)

    internal class MyGroupItem(id: Long, text: String, eventObject: IEventObject) : MyBaseItem(id, text) {
        val children: List<MyChildItem> = mutableListOf()
        val thisEvent = eventObject
    }

    internal class MyChildItem(id: Long, text: String, val odds: Double, val participantId: Long) : MyBaseItem(id, text)

    internal abstract class MyBaseViewHolder(itemView: View) : AbstractExpandableItemViewHolder(itemView)

    internal class MyGroupViewHolder(itemView: View) : MyBaseViewHolder(itemView) {
        var textView = itemView.findViewById<TextView>(R.id.group_textView)
        val mContainer = itemView.findViewById<FrameLayout>(R.id.container).setOnClickListener {
            rotateTheView(itemView.parent_down_expansion, itemView)
        }
    }

    internal class MyChildViewHolder(itemView: View) : MyBaseViewHolder(itemView) {
        var textView: TextView = itemView.findViewById(R.id.child_tv)
        var oddsTv: TextView = itemView.findViewById(R.id.odds_tv)
        var bet_button: LinearLayout = itemView.findViewById(R.id.bet_button)
    }
}