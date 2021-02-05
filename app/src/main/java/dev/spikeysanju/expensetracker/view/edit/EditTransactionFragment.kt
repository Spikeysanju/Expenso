package dev.spikeysanju.expensetracker.view.edit

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.FragmentEditTransactionBinding
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.utils.Constants
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel
import parseDouble
import transformIntoDatePicker
import java.util.*

@AndroidEntryPoint
class EditTransactionFragment : BaseFragment<FragmentEditTransactionBinding, TransactionViewModel>() {
    private val args: EditTransactionFragmentArgs by navArgs()
    override val viewModel: TransactionViewModel by activityViewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        // receiving bundles here
        val transaction = args.transaction
        initViews()
        loadData(transaction)
    }

    private fun loadData(transaction: Transaction) = with(binding.addTransactionLayout) {
        etTitle.setText(transaction.title)
        etAmount.setText(transaction.amount.toString())
        etTransactionType.setText(transaction.transactionType, false)
        etTag.setText(transaction.tag, false)
        etWhen.setText(transaction.date)
        etNote.setText(transaction.note)
    }

    private fun initViews() = with(binding) {
        val transactionTypeAdapter =
            ArrayAdapter(
                requireContext(),
                R.layout.item_autocomplete_layout,
                Constants.transactionType
            )
        val tagsAdapter = ArrayAdapter(
            requireContext(),
            R.layout.item_autocomplete_layout,
            Constants.transactionTags
        )

        // Set list to TextInputEditText adapter
        addTransactionLayout.etTransactionType.setAdapter(transactionTypeAdapter)
        addTransactionLayout.etTag.setAdapter(tagsAdapter)

        // Transform TextInputEditText to DatePicker using Ext function
        addTransactionLayout.etWhen.transformIntoDatePicker(
            requireContext(),
            "dd/MM/yyyy",
            Date()
        )
        btnSaveTransaction.setOnClickListener {
            binding.addTransactionLayout.apply {
                val (title, amount, transactionType, tag, date, note) =
                    getTransactionContent()
                // validate if transaction content is empty or not
                when {
                    title.isEmpty() -> {
                        this.etTitle.error = getString(R.string.title_must_note_be_empty)
                    }
                    amount.isNaN() -> {
                        this.etAmount.error = getString(R.string.amount_must_note_be_empty)
                    }
                    transactionType.isEmpty() -> {
                        this.etTransactionType.error = getString(R.string.transaction_type_must_note_be_empty)
                    }
                    tag.isEmpty() -> {
                        this.etTag.error = getString(R.string.tag_must_note_be_empty)
                    }
                    date.isEmpty() -> {
                        this.etWhen.error = getString(R.string.date_must_note_be_empty)
                    }
                    note.isEmpty() -> {
                        this.etNote.error = getString(R.string.note_must_note_be_empty)
                    }
                    else -> {
                        viewModel.updateTransaction(getTransactionContent()).also {
                            toast(getString(R.string.success_expense_saved)).also {
                                findNavController().popBackStack()
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getTransactionContent(): Transaction = binding.addTransactionLayout.let {

        val id = args.transaction.id
        val title = it.etTitle.text.toString()
        val amount = parseDouble(it.etAmount.text.toString())
        val transactionType = it.etTransactionType.text.toString()
        val tag = it.etTag.text.toString()
        val date = it.etWhen.text.toString()
        val note = it.etNote.text.toString()

        return Transaction(
            title = title,
            amount = amount,
            transactionType = transactionType,
            tag = tag,
            date = date,
            note = note,
            createdAt = System.currentTimeMillis(),
            id = id
        )
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentEditTransactionBinding.inflate(inflater, container, false)
}
