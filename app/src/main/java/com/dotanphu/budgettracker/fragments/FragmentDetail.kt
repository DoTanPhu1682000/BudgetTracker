package com.dotanphu.budgettracker.fragments

import android.os.Bundle
import android.view.*
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.dotanphu.budgettracker.R
import com.dotanphu.budgettracker.databinding.FragmentDetailBinding
import com.dotanphu.budgettracker.model.Transaction
import com.dotanphu.budgettracker.utils.Constants
import com.dotanphu.budgettracker.utils.transformIntoDatePicker
import com.dotanphu.budgettracker.vm.TransactionViewModel
import com.google.android.material.bottomsheet.BottomSheetDialog
import java.lang.Double.parseDouble
import java.util.*

class FragmentDetail : Fragment() {
    val transactions by navArgs<FragmentDetailArgs>()
    private lateinit var binding: FragmentDetailBinding
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentDetailBinding.inflate(inflater, container, false)
        setHasOptionsMenu(true)

        initViews()

        binding.tvLabelInput.setText(transactions.data.label)
        binding.tvAmountInput.setText(transactions.data.amount.toString())
        binding.tvDescriptionInput.setText(transactions.data.description)
        binding.edtTag.setText(transactions.data.tag, false)
        binding.etCalendar.setText(transactions.data.date)
        binding.tvType.setText(transactions.data.type, false)

        binding.imgClose.setOnClickListener {
            val action = FragmentDetailDirections.actionFragmentDetailToFragmentHome()
            findNavController().navigate(action)
        }
        return binding.root
    }

    private fun initViews() {
        val typeAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_autocomplete_layout,
            Constants.transactionType
        )
        binding.tvType.setAdapter(typeAdapter)


        val tagsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_autocomplete_layout,
            Constants.transactionTags
        )
        binding.edtTag.setAdapter(tagsAdapter)
        binding.etCalendar.transformIntoDatePicker(
            requireContext(),
            "dd/MM/yyyy",
            Date()
        )

        binding.btnUpdate.setOnClickListener {
            updateTransaction(it)
        }

    }

    private fun updateTransaction(it: View?) {
        val label = binding.tvLabelInput.text.toString()
        val amount = parseDouble(binding.tvAmountInput.text.toString())
        val des = binding.tvDescriptionInput.text.toString()
        val tag = binding.edtTag.text.toString()
        val date = binding.etCalendar.text.toString()
        val type = binding.tvType.text.toString()

        val data =
            Transaction(transactions.data.id, label = label, amount = amount, description = des, tag = tag, date = date, type = type)
        viewModel.updateTransaction(data)
        Toast.makeText(requireContext(), "Update Successfully!!!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_option, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.menu_delete) {
            val bottomSheet = BottomSheetDialog(requireContext())
            bottomSheet.setContentView(R.layout.dialog_delete)

            val textViewYes = bottomSheet.findViewById<TextView>(R.id.tv_yes)
            val textViewNo = bottomSheet.findViewById<TextView>(R.id.tv_no)

            textViewYes?.setOnClickListener {
                viewModel.deleteTransaction(transactions.data.id)
                bottomSheet.dismiss()
                val action = FragmentDetailDirections.actionFragmentDetailToFragmentHome()
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