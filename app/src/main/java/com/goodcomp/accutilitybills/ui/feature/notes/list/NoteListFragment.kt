package com.goodcomp.accutilitybills.ui.feature.notes.list

import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import by.kirich1409.viewbindingdelegate.viewBinding
import com.goodcomp.accutilitybills.R
import com.goodcomp.accutilitybills.databinding.FragmentNoteListBinding
import com.goodcomp.accutilitybills.domain.entity.CommunalPaymentNote
import com.goodcomp.accutilitybills.ui.feature.base.BaseAppFragment
import com.goodcomp.accutilitybills.ui.feature.base.BaseAppViewModel
import com.goodcomp.accutilitybills.ui.feature.notes.adapter.NoteListAdapter
import com.goodcomp.accutilitybills.util.extensions.setOnDatePickerListener
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collect
import java.text.SimpleDateFormat
import java.util.*

@AndroidEntryPoint
class NoteListFragment : BaseAppFragment<NoteListViewModel>(
    R.layout.fragment_note_list
) {

    override val viewModel: NoteListViewModel by viewModels()

    private val binding: FragmentNoteListBinding by viewBinding()

    private var adapter: NoteListAdapter? = null

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initSearchField()
        initDateTimeFilter()
        initNotesList()
        initAddNewNoteButton()
        initToStatisticButton()
    }

    override fun handleEvent(event: BaseAppViewModel.BaseEvent) {
        when (event) {
            is NoteListViewModel.Event.NavigateToEditNote -> {
                findNavController().navigate(
                    NoteListFragmentDirections
                        .actionNoteListFragmentToAddEditNoteFragment(event.note)
                )
            }
            is NoteListViewModel.Event.NavigateToAddNewNote -> {
                findNavController().navigate(
                    NoteListFragmentDirections
                        .actionNoteListFragmentToAddEditNoteFragment(null)
                )
            }
            is NoteListViewModel.Event.NavigateToStatistic -> {
                findNavController().navigate(
                    NoteListFragmentDirections
                        .actionNoteListFragmentToDatePaymentStatisticFragment()
                )
            }
            else -> super.handleEvent(event)
        }
    }

    override fun onDestroy() {
        adapter = null
        super.onDestroy()
    }

    private fun initNotesList() {
        adapter = NoteListAdapter(
            NoteListAdapter.ValueType.PAYMENT_AMOUNT,
            onItemClick = ::onNoteClick,
            resources
        ).also {
            binding.rvNoteList.adapter = it
        }
        ItemTouchHelper(
            object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ) = false
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val note = adapter?.currentList?.get(viewHolder.bindingAdapterPosition)
                    onNoteSwiped(note ?: return)
                }
            }
        ).attachToRecyclerView(binding.rvNoteList)
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.notesList.collect {
                adapter?.submitList(it)
            }
        }
    }

    private fun onNoteClick(note: CommunalPaymentNote) {
        viewModel.onNoteClick(note)
    }

    private fun onNoteSwiped(note: CommunalPaymentNote) {
        viewModel.onNoteSwiped(note)
    }

    private fun initDateTimeFilter() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            binding.tvStartDate.setOnDatePickerListener(
                requireContext(),
                viewModel.startDateTime
            ) {
                viewModel.onStartDatePicked(it)
            }
            binding.tvEndDate.setOnDatePickerListener(
                requireContext(),
                viewModel.endDateTime
            ) {
                viewModel.onEndDatePicked(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.startDateTime.collect {
                binding.tvStartDate.text = formatCalendarToString(it)
            }
        }
        viewLifecycleOwner.lifecycleScope.launchWhenStarted {
            viewModel.endDateTime.collect {
                binding.tvEndDate.text = formatCalendarToString(it)
            }
        }
    }

    private fun initSearchField() {
        binding.etSearchNoteByType.setText(viewModel.searchText.value)
        binding.etSearchNoteByType.addTextChangedListener {
            viewModel.onSearchTextChanged(it.toString())
        }
    }

    private fun initAddNewNoteButton() {
        binding.fabAddNewNote.setOnClickListener {
            viewModel.onAddNewNoteButtonClick()
        }
    }

    private fun initToStatisticButton() {
        binding.fabToStatistic.setOnClickListener {
            viewModel.onToStatisticButtonClick()
        }
    }

    private fun formatCalendarToString(calendar: Calendar) =
        SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())
            .format(Date(calendar.timeInMillis))

    companion object {
        private const val DATE_FORMAT_PATTERN = "dd.MM.yyyy"
    }
}