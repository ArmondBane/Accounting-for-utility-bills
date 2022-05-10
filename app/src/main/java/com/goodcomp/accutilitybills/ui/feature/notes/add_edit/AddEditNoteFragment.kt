package com.goodcomp.accutilitybills.ui.feature.notes.add_edit

import android.app.TimePickerDialog
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.view.doOnLayout
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import by.kirich1409.viewbindingdelegate.viewBinding
import com.goodcomp.accutilitybills.R
import com.goodcomp.accutilitybills.databinding.FragmentAddEditNoteBinding
import com.goodcomp.accutilitybills.domain.entity.PaymentType
import com.goodcomp.accutilitybills.ui.feature.base.BaseAppFragment
import com.goodcomp.accutilitybills.ui.feature.base.BaseAppViewModel
import com.goodcomp.accutilitybills.ui.feature.notes.adapter.NoteListAdapter
import com.goodcomp.accutilitybills.util.extensions.setOnDatePickerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.collectLatest
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class AddEditNoteFragment : BaseAppFragment<AddEditNoteViewModel>(
    R.layout.fragment_add_edit_note
) {

    override val viewModel: AddEditNoteViewModel by viewModels()

    private val binding: FragmentAddEditNoteBinding by viewBinding()

    private var adapter: NoteListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initHeader()
        initDateTimeField()
        initPaymentTypeField()
        initIsCounterAvailableField()
        initMeterReadingField()
        initPaymentAmountField()
        initLastNotesList()
        initSaveButton()
    }

    override fun handleEvent(event: BaseAppViewModel.BaseEvent) {
        when (event) {
            is AddEditNoteViewModel.Event.NavigateBack ->
                findNavController().popBackStack()
            else -> super.handleEvent(event)
        }
    }

    override fun onDestroy() {
        adapter = null
        super.onDestroy()
    }

    private fun initSaveButton() {
        binding.btnSave.text = getString(viewModel.buttonTextRes)
        binding.btnSave.setOnClickListener {
            viewModel.onSaveButtonClick()
        }
    }

    private fun initLastNotesList() {
        adapter = NoteListAdapter(
            NoteListAdapter.ValueType.METERS_READING,
            onItemClick = { },
            resources
        ).also {
            binding.rvPreviousMeterReadings.adapter = it
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.previousMeterReadingsList.collect {
                adapter?.submitList(it)
            }
        }
    }

    private fun initPaymentAmountField() {
        if (viewModel.paymentAmount.value != 0.0)
            binding.etPaymentAmount.setText(viewModel.paymentAmount.value.toString())
        binding.etPaymentAmount.addTextChangedListener {
            viewModel.onPaymentAmountTextChanged(it.toString())
        }
    }

    private fun initMeterReadingField() {
        if (viewModel.meterReading.value != 0)
            binding.etMeterReading.setText(viewModel.meterReading.value.toString())
        binding.etMeterReading.addTextChangedListener {
            viewModel.onMeterReadingTextChanged(it.toString())
        }
    }

    private fun initIsCounterAvailableField() {
        binding.cbCounterAvailable.isChecked = viewModel.isCounterAvailable.value
        binding.cbCounterAvailable.setOnCheckedChangeListener { _, _ ->
            viewModel.onCounterAvailableCheckChanged(
                binding.cbCounterAvailable.isChecked
            )
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.isCounterAvailable.collect {
                binding.cbCounterAvailable.text =
                    if (it) getString(R.string.title_is_counter_available_yes)
                    else getString(R.string.title_is_counter_available_no)
            }
        }
    }

    private fun initPaymentTypeField() {
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.allPaymentsType.collect { types ->
                if (binding.sprPaymentType.adapter != null) return@collect
                binding.sprPaymentType.adapter = ArrayAdapter(
                    requireContext(),
                    android.R.layout.simple_spinner_item,
                    types.map { it.name }
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
                            val type = if (selectedItemPosition < types.count())
                                types[selectedItemPosition]
                            else return
                            viewModel.onPaymentTypeSelected(type)
                        }
                        override fun onNothingSelected(parent: AdapterView<*>?) = Unit
                    }
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.paymentTypeIndex.collect { index ->
                binding.sprPaymentType.doOnLayout {
                    if (index != -1) binding.sprPaymentType.setSelection(index)
                }
            }
        }
    }

    private fun initDateTimeField() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N)
            binding.tvDateTimePicker.setOnDatePickerListener(
                requireContext(),
                viewModel.dateTime
            ) { calendar ->
                showTimePickerDialog(calendar)
            }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.dateTime.collect {
                binding.tvDateTimePicker.text = formatCalendarToString(it)
            }
        }
    }

    private fun showTimePickerDialog(calendar: Calendar) {
        TimePickerDialog(
            requireContext(),
            { _, hourOfDay, minute ->
                viewModel.onDateTimeChanged(
                    calendar.apply {
                        set(Calendar.HOUR_OF_DAY, hourOfDay)
                        set(Calendar.MINUTE, minute)
                    }
                )
            },
            viewModel.dateTime.value.get(Calendar.HOUR),
            viewModel.dateTime.value.get(Calendar.MINUTE),
            true
        ).show()
    }

    private fun initHeader() {
        binding.tvTitle.text = getString(viewModel.headerTextRes)
    }

    private fun formatCalendarToString(calendar: Calendar) =
        SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())
            .format(Date(calendar.timeInMillis))

    companion object {
        private const val DATE_FORMAT_PATTERN = "dd.MM.yyyy HH:mm"
    }
}