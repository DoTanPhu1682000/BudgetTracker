package com.dotanphu.budgettracker.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.dotanphu.budgettracker.databinding.FragmentDeleteSuccessfulBinding

class FragmentDeleteSuccessful:Fragment() {
    private lateinit var binding: FragmentDeleteSuccessfulBinding
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding= FragmentDeleteSuccessfulBinding.inflate(inflater,container,false)

        binding.btnBack.setOnClickListener {
            val action = FragmentDeleteSuccessfulDirections.actionFragmentDeleteSuccessfulToFragmentHome()
            findNavController().navigate(action)
        }
        return binding.root
    }
}