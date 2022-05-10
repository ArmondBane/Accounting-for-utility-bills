package com.goodcomp.accutilitybills.ui.feature.notes.adapter

import android.content.res.Resources
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.goodcomp.accutilitybills.R
import com.goodcomp.accutilitybills.databinding.ItemNoteListBinding
import com.goodcomp.accutilitybills.domain.entity.CommunalPaymentNote
import java.text.SimpleDateFormat
import java.util.*

class NoteListAdapter(
    private val paymentType: ValueType,
    private val onItemClick: (note: CommunalPaymentNote) -> Unit,
    private val resources: Resources
) : ListAdapter<CommunalPaymentNote, NoteListAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int) =
        ViewHolder(
            ItemNoteListBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(
        private val binding: ItemNoteListBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(item: CommunalPaymentNote) {
            binding.llContainer.setOnClickListener { onItemClick(item) }
            binding.tvTypeName.text = SimpleDateFormat(DATE_FORMAT_PATTERN, Locale.getDefault())
                    .format(Date(item.dateTime))
            when (paymentType) {
                ValueType.PAYMENT_AMOUNT -> {
                    binding.tvPaymentAmount.text =
                        resources.getString(R.string.rub, item.paymentAmount)
                }
                ValueType.METERS_READING -> {
                    binding.tvPaymentAmount.text = item.meterReading.toString()
                }
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<CommunalPaymentNote>() {
        override fun areItemsTheSame(
            oldItem: CommunalPaymentNote,
            newItem: CommunalPaymentNote
        ): Boolean {
            return oldItem.dateTime == newItem.dateTime
        }

        override fun areContentsTheSame(
            oldItem: CommunalPaymentNote,
            newItem: CommunalPaymentNote
        ): Boolean {
            return oldItem == newItem
        }
    }

    enum class ValueType {
        PAYMENT_AMOUNT,
        METERS_READING
    }

    companion object {
        private const val DATE_FORMAT_PATTERN = "dd.MM.yy"
    }
}