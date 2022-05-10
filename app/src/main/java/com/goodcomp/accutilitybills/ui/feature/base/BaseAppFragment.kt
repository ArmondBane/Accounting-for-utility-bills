package com.goodcomp.accutilitybills.ui.feature.base

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.annotation.LayoutRes
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.flow.collect

abstract class BaseAppFragment<VM: BaseAppViewModel>(
    @LayoutRes layoutRes: Int
) : Fragment(layoutRes) {

    protected abstract val viewModel: VM

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.event.collect {
                handleEvent(it)
            }
        }
    }

    open fun handleEvent(event: BaseAppViewModel.BaseEvent) =
        when (event) {
            is BaseAppViewModel.ShowToastMassage -> {
                Toast.makeText(
                    requireContext(),
                    event.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
            else -> Unit
        }
}