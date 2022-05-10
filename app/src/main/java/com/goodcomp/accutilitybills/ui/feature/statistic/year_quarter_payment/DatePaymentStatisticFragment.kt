package com.goodcomp.accutilitybills.ui.feature.statistic.year_quarter_payment

import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import by.kirich1409.viewbindingdelegate.viewBinding
import com.goodcomp.accutilitybills.R
import com.goodcomp.accutilitybills.databinding.FragmentDatePaymentStatisticBinding
import com.goodcomp.accutilitybills.ui.feature.base.BaseAppFragment
import com.goodcomp.accutilitybills.util.extensions.setOnDatePickerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.util.*

@AndroidEntryPoint
class DatePaymentStatisticFragment : BaseAppFragment<DatePaymentStatisticViewModel>(
    R.layout.fragment_date_payment_statistic
) {

    override val viewModel: DatePaymentStatisticViewModel by viewModels()

    private val binding: FragmentDatePaymentStatisticBinding by viewBinding()

    private val selectedQuarter
        get() = DatePaymentStatisticViewModel.Quarter.values().indexOf(viewModel.quarter.value)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initYearPicker()
        initQuarterPicker()
        initAveragePayment()
    }

    private fun initAveragePayment() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.statisticValue.collect {
                binding.tvAveragePayment.text = getString(R.string.rub, it ?: 0.0)
            }
        }
    }

    private fun initQuarterPicker() {
        binding.sprPaymentType.adapter = ArrayAdapter(
            requireContext(),
            android.R.layout.simple_spinner_item,
            DatePaymentStatisticViewModel.Quarter.values().map { it.name }
        ).apply {
            setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        }
        binding.sprPaymentType.onItemSelectedListener =
            object : AdapterView.OnItemSelectedListener {
                override fun onItemSelected(
                    parent: AdapterView<*>?,
                    view: View?,
                    selectedItemPosition: Int,
                    selectedId: Long
                ) {
                    if(view == null) return
                    val quarters = DatePaymentStatisticViewModel.Quarter.values()
                    val quarter = if (selectedItemPosition < quarters.count())
                        quarters[selectedItemPosition]
                    else return
                    viewModel.onQuarterPicked(quarter)
                }
                override fun onNothingSelected(parent: AdapterView<*>?) = Unit
            }
        selectedQuarter.let { index ->
            if (index != -1) binding.sprPaymentType.setSelection(index)
        }
    }

    private fun initYearPicker() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvYear.setOnDatePickerListener(
                requireContext(),
                viewModel.year
            ) {
                viewModel.onYearWasPicked(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.year.collect {
                binding.tvYear.text = formatCalendarToString(it)
            }
        }
    }

    private fun formatCalendarToString(calendar: Calendar) =
        "${calendar.get(Calendar.YEAR)}"
}