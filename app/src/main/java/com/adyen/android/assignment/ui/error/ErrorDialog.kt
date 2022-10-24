package com.adyen.android.assignment.ui.error

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import com.adyen.android.assignment.R
import com.adyen.android.assignment.databinding.ActivityErrorBinding
import com.adyen.android.assignment.ui.ouruniverse.PodsViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class ErrorDialog : DialogFragment() {

    private var _binding: ActivityErrorBinding? = null

    private val binding get() = _binding!!

    private val podsViewModel: PodsViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        _binding = ActivityErrorBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.refreshBtn.setOnClickListener {
            dismiss()

            podsViewModel.refreshList()
        }

    }

    override fun getTheme(): Int {
        return R.style.DialogTheme
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}