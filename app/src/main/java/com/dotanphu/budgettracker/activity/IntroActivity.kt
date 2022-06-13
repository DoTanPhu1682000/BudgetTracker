package com.dotanphu.budgettracker.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.activity.viewModels
import androidx.fragment.app.viewModels
import com.dotanphu.budgettracker.databinding.ActivityIntroBinding
import com.dotanphu.budgettracker.vm.TransactionViewModel

class IntroActivity : AppCompatActivity() ,Runnable{
    private lateinit var binding: ActivityIntroBinding
    private val viewModel: TransactionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityIntroBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.getTotalAmountByType()

        Thread(this).start()
    }

    override fun run() {
        Thread.sleep(3000)
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}