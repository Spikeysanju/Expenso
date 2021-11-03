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
import snack
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

    private fun loadData(transaction: Transaction) = with(binding) {
        addTransactionLayout.etTitle.setText(transaction.title)
        addTransactionLayout.etAmount.setText(transaction.amount.toString())
        addTransactionLayout.etTransactionType.setText(transaction.transactionType, false)
        addTransactionLayout.etTag.setText(transaction.tag, false)
        addTransactionLayout.etWhen.setText(transaction.date)
        addTransactionLayout.etNote.setText(transaction.note)
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
                        this.etTitle.error = "Title must not be empty"
                    }
                    amount.isNaN() -> {
                        this.etAmount.error = "Amount must not be empty"
                    }
                    transactionType.isEmpty() -> {
                        this.etTransactionType.error = "Transaction type must not be empty"
                    }
                    tag.isEmpty() -> {
                        this.etTag.error = "Tag must not be empty"
                    }
                    date.isEmpty() -> {
                        this.etWhen.error = "Date must not be empty"
                    }
                    note.isEmpty() -> {
                        this.etNote.error = "Note must not be empty"
                    }
                    else -> {
                        viewModel.updateTransaction(getTransactionContent()).also {

                            binding.root.snack(
                                string = R.string.success_expense_saved
                            ).run {
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
