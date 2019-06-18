package com.example.robmillaci.myapplication.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.fragment.app.FragmentActivity
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView
import com.example.robmillaci.myapplication.R
import com.example.robmillaci.myapplication.activities.fragments.home_activity.HomeActivityViewModel
import com.example.robmillaci.myapplication.databinding.BetSlipEventBinding
import com.example.robmillaci.myapplication.extension_functions.determineImageDrawable
import com.example.robmillaci.myapplication.pojos.IEventObject
import java.lang.ref.WeakReference

class BetSlipAdapter(val weakContext: WeakReference<Context>, val items: ArrayList<IEventObject>?) :
    RecyclerView.Adapter<BetSlipAdapter.MyBetSlipViewHolder>() {
    var mViewModel: HomeActivityViewModel =
        ViewModelProviders.of(weakContext.get() as FragmentActivity).get(HomeActivityViewModel::class.java)


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyBetSlipViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return MyBetSlipViewHolder(BetSlipEventBinding.inflate(inflater,parent,false))
    }

    override fun getItemCount(): Int {
        return items?.size ?: 0
    }

    override fun onBindViewHolder(holder: MyBetSlipViewHolder, position: Int) {
        items?.get(position)?.let { holder.bind(it) }

        holder.bet_icon.setImageDrawable(
            holder.bet_icon.determineImageDrawable(
                weakContext,
                items?.get(position)?.getSpecificTypes()!!
            )
        )

        holder.entryNumber.text = (holder.adapterPosition + 1).toString()

        holder.deleteBet.setOnClickListener {
            val itemToRemove = items.get(holder.adapterPosition)
            mViewModel.updateBetSlip(itemToRemove, holder.adapterPosition, false)
            notifyItemRemoved(holder.adapterPosition);
        }
    }


    class MyBetSlipViewHolder(val itemBinding: BetSlipEventBinding) : RecyclerView.ViewHolder(itemBinding.root) {
        fun bind(items: IEventObject) {
            itemBinding.item = items
            itemBinding.executePendingBindings()
        }

        val bet_icon : ImageView = itemBinding.root.findViewById(R.id.betslip_entry_icon)
        val entryNumber: TextView = itemBinding.root.findViewById(R.id.entry_number)
        val deleteBet: ImageView = itemBinding.root.findViewById(R.id.delete_bet)
    }


}

