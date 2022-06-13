package com.dotanphu.budgettracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.Navigation
import androidx.navigation.fragment.findNavController
import com.dotanphu.budgettracker.R
import com.dotanphu.budgettracker.databinding.FragmentAddTransactionBinding
import com.dotanphu.budgettracker.model.Transaction
import com.dotanphu.budgettracker.utils.Constants
import com.dotanphu.budgettracker.utils.transformIntoDatePicker
import com.dotanphu.budgettracker.vm.TransactionViewModel
import java.lang.Double.parseDouble
import java.util.*

class FragmentAddTransaction : Fragment() {
    private lateinit var binding: FragmentAddTransactionBinding
    private val viewModel: TransactionViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = FragmentAddTransactionBinding.inflate(inflater, container, false)

        initView()

        binding.btnClose.setOnClickListener {
            val action = FragmentAddTransactionDirections.actionFragmentAddTransactionToFragmentHome()
            findNavController().navigate(action)
        }

        return binding.root
    }

    private fun initView() {

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

        binding.btnAdd.setOnClickListener {
            binding.btnAdd.apply {
                createTransaction()
            }
        }
    }

    private fun createTransaction() {
        val label = binding.tvLabelInput.text.toString()
        val amount = parseDouble(binding.tvAmountInput.text.toString())
        val des = binding.tvDescriptionInput.text.toString()
        val tag = binding.edtTag.text.toString()
        val date = binding.etCalendar.text.toString()
        val type = binding.tvType.text.toString()

        val data = Transaction(0, label = label, amount = amount, description = des, tag = tag, date = date, type = type)
        viewModel.insertTransaction(data)
        Toast.makeText(requireContext(), "Create Successfully!!!", Toast.LENGTH_SHORT).show()
        findNavController().popBackStack()
    }
}