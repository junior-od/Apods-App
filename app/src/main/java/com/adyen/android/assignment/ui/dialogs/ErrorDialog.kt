package com.adyen.android.assignment.ui.dialogs

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.DialogFragment
import com.adyen.android.assignment.R
import com.adyen.android.assignment.databinding.ActivityErrorBinding
import com.adyen.android.assignment.ui.interfaces.RefreshCallbackListener

class ErrorDialog (private val refreshCallbackListener: RefreshCallbackListener): DialogFragment() {

    private var _binding: ActivityErrorBinding? = null

    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        isCancelable = false
        _binding = ActivityErrorBinding.inflate(LayoutInflater.from(context))
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.refreshBtn.setOnClickListener {
            dismiss()
            refreshCallbackListener.onRefresh()
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