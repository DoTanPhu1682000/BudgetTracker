package com.dotanphu.budgettracker.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.navigation.Navigation
import androidx.recyclerview.widget.RecyclerView
import com.dotanphu.budgettracker.R
import com.dotanphu.budgettracker.databinding.ItemTransactionBinding
import com.dotanphu.budgettracker.fragments.FragmentHomeDirections
import com.dotanphu.budgettracker.model.Transaction
import kotlin.collections.ArrayList

class TransactionAdapter(val requireContext: Context, private var transactions: List<Transaction>) :
    RecyclerView.Adapter<TransactionAdapter.ViewHolder>() {

    fun filter(newFilterList: ArrayList<Transaction>) {
        transactions = newFilterList
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemTransactionBinding) :
        RecyclerView.ViewHolder(binding.root) {
    }


    override fun onCreateViewHolder(
        parent: ViewGroup, viewType: Int
    ): TransactionAdapter.ViewHolder {
        return ViewHolder(ItemTransactionBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: TransactionAdapter.ViewHolder, position: Int) {
        val data = transactions[position]
        holder.binding.apply {
            holder.binding.tvLabel.text = data.label

            when (data.type) {
                "Income" -> {
                    tvAmount.setTextColor(
                        ContextCompat.getColor(
                            tvAmount.context,
                            R.color.green
                        )
                    )

                    tvAmount.text = "+ $%.2f".format(data.amount)
                }
                "Expense" -> {
                    tvAmount.setTextColor(
                        ContextCompat.getColor(
                            tvAmount.context,
                            R.color.red
                        )
                    )
                    tvAmount.text = "- $%.2f".format(data.amount)
                }
            }

            when (data.tag) {
                "Transportation" -> {
                    imgTransaction.setImageResource(R.drawable.ic_transport)
                }
                "Food" -> {
                    imgTransaction.setImageResource(R.drawable.ic_fastfood)
                }
                "Healthcare" -> {
                    imgTransaction.setImageResource(R.drawable.ic_health_and_safety)
                }
                "Home" -> {
                    imgTransaction.setImageResource(R.drawable.ic_baseline_home_24)
                }
                else -> {
                    imgTransaction.setImageResource(R.drawable.ic_others)
                }
            }

            holder.binding.root.setOnClickListener {
                val action = FragmentHomeDirections.actionFragmentHomeToFragmentDetail(data)
                Navigation.findNavController(it).navigate(action)
            }
        }
    }

    override fun getItemCount() = transactions.size

    fun setData(listData: List<Transaction>) {
        this.transactions = listData
        notifyDataSetChanged()
    }
}