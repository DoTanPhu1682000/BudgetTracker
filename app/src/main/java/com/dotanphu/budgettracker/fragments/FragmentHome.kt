package com.dotanphu.budgettracker.fragments

import android.app.DatePickerDialog
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.view.*
import android.widget.SearchView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.dotanphu.budgettracker.R
import com.dotanphu.budgettracker.adapter.TransactionAdapter
import com.dotanphu.budgettracker.databinding.FragmentHomeBinding
import com.dotanphu.budgettracker.db.TransactionDatabase
import com.dotanphu.budgettracker.model.Transaction
import com.dotanphu.budgettracker.vm.TransactionViewModel
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.formatter.PercentFormatter
import com.google.android.material.bottomsheet.BottomSheetDialog

class FragmentHome : Fragment() {
    private lateinit var pieChart: PieChart
    private lateinit var binding: FragmentHomeBinding
    private val viewModel: TransactionViewModel by viewModels()
    var oldMyTransaction = arrayListOf<Transaction>()
    private lateinit var adapter: TransactionAdapter
    private var income: Float = 0F
    private var expense: Float = 0F
    private var onClickDate = false

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentHomeBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        setupRV()

        binding.btnAdd.setOnClickListener {
            createNewTransaction()
        }

//        pieChart = binding.pieChart

        viewModel.getTotalAmountByType()

//        setupPieChart()
//        loadPieChartData(income,expense)

        return binding.root
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


    private fun setupRV() {
        val staggeredGridLayoutManager = StaggeredGridLayoutManager(1, LinearLayoutManager.VERTICAL)
        binding.rvHome.layoutManager = staggeredGridLayoutManager

        viewModel.getAllTransaction().observe(viewLifecycleOwner) { transactionList ->
            oldMyTransaction = transactionList as ArrayList<Transaction>
            adapter = TransactionAdapter(requireActivity(), transactionList)
            binding.rvHome.adapter = adapter
        }

        viewModel.income.observe(viewLifecycleOwner) {
            binding.tvBudget.text = "$ %.2f".format(it)
            income = it.toFloat()

//            loadPieChartData(income, expense)
            if (viewModel.expense.value != null) {
                val balance = viewModel.income.value!! - it
                binding.tvBalance.text = "$ %.2f".format(balance)
            }
        }

        viewModel.expense.observe(viewLifecycleOwner) {
            binding.tvExpense.text = "$ %.2f".format(it)
            expense = it.toFloat()

//            loadPieChartData(income, expense)
            if (viewModel.income.value != null) {
                val balance = viewModel.income.value!! - it
                binding.tvBalance.text = "$ %.2f".format(balance)
            }
        }

    }

    private fun createNewTransaction() {
        val action = FragmentHomeDirections.actionFragmentHomeToFragmentAddTransaction()
        findNavController().navigate(action)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.search_menu, menu)

        val item = menu.findItem(R.id.app_bar_search)
        val searchView = item.actionView as SearchView
        searchView.queryHint = "Enter Transaction Here..."
        searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(p0: String?): Boolean {
                return false
            }

            override fun onQueryTextChange(p0: String?): Boolean {
                TransactionFilter(p0)
                return true
            }
        })

        super.onCreateOptionsMenu(menu, inflater)
    }

    private fun TransactionFilter(p0: String?) {
        val newFilterList = arrayListOf<Transaction>()

        for (i in oldMyTransaction) {
            if (i.label.contains(p0!!) || i.date.contains(p0!!)) {
                newFilterList.add(i)
            }
        }
        adapter.filter(newFilterList)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.clear_all) {
            val bottomSheet = BottomSheetDialog(requireContext())
            bottomSheet.setContentView(R.layout.dialog_delete_all)

            val textViewYes = bottomSheet.findViewById<TextView>(R.id.tv_yes)
            val textViewNo = bottomSheet.findViewById<TextView>(R.id.tv_no)

            textViewYes?.setOnClickListener {
                viewModel.deleteAllTransaction()
                bottomSheet.dismiss()
                val action = FragmentHomeDirections.actionFragmentHomeToFragmentDeleteSuccessful()
                findNavController().navigate(action)
            }
            textViewNo?.setOnClickListener {
                bottomSheet.dismiss()
            }

            bottomSheet.show()
        }
        return super.onOptionsItemSelected(item)
    }
}
