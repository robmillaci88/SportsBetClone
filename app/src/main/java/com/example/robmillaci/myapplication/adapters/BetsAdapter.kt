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

/**
 * The adapter for the different types of bets that can be chosen within the Sports betting activity
 * uses 3rd party library sectionedrecyclerviewadapter
 */
internal class BetsAdapter(groupItems: List<MyGroupItem>, private val itemDecoration: RecyclerView.ItemDecoration,
    private val viewBetClickedListener: IViewbetClickedListener) :
    AbstractExpandableItemAdapter<BetsAdapter.MyGroupViewHolder, BetsAdapter.MyChildViewHolder>() {

    private var mItems: List<MyGroupItem>? = groupItems
    private var recyclerView: RecyclerView? = null

    init {
        setHasStableIds(true)
    }

    override fun onCreateGroupViewHolder(parent: ViewGroup?, viewType: Int): MyGroupViewHolder {
        return MyGroupViewHolder(R.layout.bet_type_recycler_view.inflate(parent?.context!!, parent))
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

        holder.bet_button.setOnClickListener {
            val thisEventObject = parent.thisEvent
            viewBetClickedListener.betClicked(holder.bet_button, thisEventObject, parent.text)
        }
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

        if (expand) {
            recyclerView?.removeItemDecoration(itemDecoration)
        } else {
            recyclerView?.addItemDecoration(itemDecoration)
        }
        return true
    }


    interface IViewbetClickedListener {
        fun betClicked(view: View, eventObject: IEventObject, betName: String)
    }

    internal abstract class MyBaseItem(val id: Long, val text: String)

    internal class MyGroupItem(id: Long, text: String, eventObject: IEventObject) : MyBaseItem(id, text) {
        val children: List<MyChildItem> = mutableListOf()
        val thisEvent = eventObject
    }

    internal class MyChildItem(id: Long, text: String, val odds: Double) : MyBaseItem(id, text)

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