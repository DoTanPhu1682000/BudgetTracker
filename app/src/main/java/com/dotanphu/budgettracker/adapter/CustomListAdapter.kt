package com.dotanphu.budgettracker.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.dotanphu.budgettracker.databinding.ItemCustomDilogBinding
import com.dotanphu.budgettracker.fragments.FragmentAllTransaction

class CustomListAdapter(val fragment: Fragment, private val customList:List<String>): RecyclerView.Adapter<CustomListAdapter.ViewHolder>() {

    class ViewHolder(itemView: ItemCustomDilogBinding):RecyclerView.ViewHolder(itemView.root) {
        val title=itemView.tvText

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding=ItemCustomDilogBinding.inflate(LayoutInflater.from(fragment.requireContext()),parent,false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item=customList[position]
        holder.title.text=item
        holder.itemView.setOnClickListener {
            if (fragment is FragmentAllTransaction){
                fragment.filterTransactions(item)
            }
        }

    }

    override fun getItemCount(): Int {
        return customList.size
    }
}