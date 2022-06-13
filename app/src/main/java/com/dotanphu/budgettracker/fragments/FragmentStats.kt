package com.dotanphu.budgettracker.fragments

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.dotanphu.budgettracker.adapter.TransactionAdapter
import com.dotanphu.budgettracker.databinding.FragmentStatsBinding
import com.dotanphu.budgettracker.vm.TransactionViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter

class FragmentStats : Fragment() {
    private lateinit var pieChart: PieChart
    private lateinit var binding: FragmentStatsBinding
    private val viewModel: TransactionViewModel by viewModels()
    private lateinit var adapter: TransactionAdapter
    private var income: Float = 0F
    private var expense: Float = 0F

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentStatsBinding.inflate(layoutInflater)

        setUp()

        pieChart = binding.pieChart

        viewModel.getTotalAmountByType()

        setupPieChart()
        loadPieChartData(income, expense)

        return binding.root
    }

    private fun setUp() {
        viewModel.income.observe(viewLifecycleOwner) {
            binding.tvBudget.text = "$ %.2f".format(it)
            income = it.toFloat()

            loadPieChartData(income, expense)
            if (viewModel.expense.value != null) {
                val balance = viewModel.income.value!! - it
                binding.tvBalance.text = "$ %.2f".format(balance)
            }
        }

        viewModel.expense.observe(viewLifecycleOwner) {
            binding.tvExpense.text = "$ %.2f".format(it)
            expense = it.toFloat()

            loadPieChartData(income, expense)
            if (viewModel.income.value != null) {
                val balance = viewModel.income.value!! - it
                binding.tvBalance.text = "$ %.2f".format(balance)
            }
        }
    }

    private fun loadPieChartData(income: Float, expend: Float) {
        val entries = ArrayList<PieEntry>()
        entries.add(PieEntry(income, "income"))
        entries.add(PieEntry(expend, "expense"))
        val colors = ArrayList<Int>()
        colors.add(Color.GREEN)
        colors.add(Color.RED)
        val dataSet = PieDataSet(entries, "")
        dataSet.colors = colors
        val data = PieData(dataSet)
        data.setDrawValues(true)
        data.setValueFormatter(PercentFormatter(pieChart))
        data.setValueTextSize(12f)
        data.setValueTextColor(Color.BLACK)
        pieChart.data = data
    }

    private fun setupPieChart() {
        pieChart.isDrawHoleEnabled = true
        pieChart.setUsePercentValues(true)
        pieChart.setEntryLabelTextSize(12f)
        pieChart.setEntryLabelColor(Color.BLACK)
        pieChart.centerText = "Balance"
        pieChart.setCenterTextSize(13f)

        pieChart.description.isEnabled = false
        val l = pieChart.legend
        l.verticalAlignment = Legend.LegendVerticalAlignment.TOP
        l.horizontalAlignment = Legend.LegendHorizontalAlignment.RIGHT
        l.orientation = Legend.LegendOrientation.VERTICAL
        l.setDrawInside(false)
        l.isEnabled = true
    }
}