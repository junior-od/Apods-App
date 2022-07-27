package com.adyen.android.assignment.ui

import android.content.Intent
import android.os.Bundle
import com.adyen.android.assignment.databinding.ActivityErrorBinding
import com.adyen.android.assignment.ui.commons.BaseActivity
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorActivity : BaseActivity() {
    private lateinit var binding: ActivityErrorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityErrorBinding.inflate(layoutInflater)

        enterFullScreen()
        setContentView(binding.root)

        binding.refreshBtn.setOnClickListener {
            onBackPressed()
        }
    }

    override fun onBackPressed() {
        val intent = Intent()
        setResult(RESULT_OK, intent)
        finish()
    }
}