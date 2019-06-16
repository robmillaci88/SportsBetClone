package com.example.robmillaci.myapplication.adapters

import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.extension_functions.inflate
import com.example.robmillaci.myapplication.miscs.rotateTheView
import com.example.robmillaci.myapplication.pojos.IEventObject
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemAdapter
import com.h6ah4i.android.widget.advrecyclerview.utils.AbstractExpandableItemViewHolder
import kotlinx.android.synthetic.main.bet_type_recycler_view.view.*

internal class BetsAdapter(groupItems : List<MyGroupItem>, val itemDecoration : RecyclerView.ItemDecoration,val viewBetClickedListener : IViewbetClickedListener) :
    AbstractExpandableItemAdapter<BetsAdapter.MyGroupViewHolder, BetsAdapter.MyChildViewHolder>(){
    var mItems: List<MyGroupItem>? = groupItems
    var recyclerView : RecyclerView? = null
    init {
        setHasStableIds(true)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): BetsAdapter.MyGroupViewHolder {
        return MyGroupViewHolder(R.layout.bet_type_recycler_view.inflate(parent?.context!!,parent))
    }

    override fun onAttachedToRecyclerView(recyclerView: RecyclerView) {
        super.onAttachedToRecyclerView(recyclerView)
        this.recyclerView = recyclerView
    }

    override fun onCreateChildViewHolder(parent: ViewGroup?, viewType: Int): BetsAdapter.MyChildViewHolder {
        return MyChildViewHolder(R.layout.bet_type_children_view.inflate(parent?.context!!,parent))
    }

    override fun onBindGroupViewHolder(
        holder: BetsAdapter.MyGroupViewHolder,
        groupPosition: Int,
        viewType: Int
    ) {
        val group = mItems?.get(groupPosition)
        holder.textView.text = group?.text
    }

    override fun onBindChildViewHolder(
        holder: BetsAdapter.MyChildViewHolder,
        groupPosition: Int,
        childPosition: Int,
        viewType: Int
    ) {
        val child = mItems?.get(groupPosition)?.children!![childPosition]
        holder.textView.text = child.text
        holder.oddsTv.text = child.odds.toString()
        holder.bet_button.setOnClickListener {
            val thisEventObject = mItems?.get(groupPosition)!!.thisEvent
            viewBetClickedListener.betClicked(holder.bet_button,thisEventObject)
        }
    }

    override fun getChildId(groupPosition: Int, childPosition: Int): Long {
        return mItems?.get(groupPosition)?.children?.get(childPosition)?.id!!;
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
        holder: BetsAdapter.MyGroupViewHolder,
        groupPosition: Int,
        x: Int,
        y: Int,
        expand: Boolean
    ): Boolean {

        if(expand) {
            recyclerView?.removeItemDecoration(itemDecoration)
        }else {
            recyclerView?.addItemDecoration(itemDecoration)
        }
        return true
    }


interface IViewbetClickedListener{
    fun betClicked(view : View, eventObject : IEventObject)
}

internal abstract class MyBaseItem(val id: Long, val text: String)
internal class MyGroupItem(id: Long, text: String, eventObject: IEventObject) : MyBaseItem(id, text) {
    val children: List<MyChildItem>  = mutableListOf<MyChildItem>()
    val thisEvent = eventObject
}
internal class MyChildItem(id: Long, text: String, val odds : Double) : MyBaseItem(id, text)
internal abstract class MyBaseViewHolder(itemView: View) : AbstractExpandableItemViewHolder(itemView)


internal class MyGroupViewHolder(itemView: View) : MyBaseViewHolder(itemView){
    var textView = itemView.findViewById<TextView>(R.id.group_textView)
    val mContainer = itemView.findViewById<FrameLayout>(R.id.container).setOnClickListener {
       rotateTheView(itemView.parent_down_expansion,itemView)
    }
}

internal class MyChildViewHolder(itemView: View) : MyBaseViewHolder(itemView){
    var textView = itemView.findViewById<TextView>(R.id.child_tv)
    var oddsTv = itemView.findViewById<TextView>(R.id.odds_tv)
    var bet_button = itemView.findViewById<LinearLayout>(R.id.bet_button)
}
}