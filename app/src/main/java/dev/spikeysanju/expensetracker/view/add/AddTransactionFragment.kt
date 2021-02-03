package dev.spikeysanju.expensetracker.view.add

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.FragmentAddTransactionBinding
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.utils.Constants
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel
import transformIntoDatePicker
import java.util.*

@AndroidEntryPoint
class AddTransactionFragment :
    BaseFragment<FragmentAddTransactionBinding, TransactionViewModel>() {
    override val viewModel: TransactionViewModel by activityViewModels()
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
    }

    private fun initViews() {

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

        with(binding) {
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
                    val (title, amount, transactionType, tag, date, note) = getTransactionContent()
                    // validate if transaction content is empty or not
                    when {
                        title.isEmpty() -> {
                            this.etTitle.error = "Title must note be empty"
                        }
                        amount.toString().isEmpty() -> {
                            this.etAmount.error = "Amount must note be empty"
                        }
                        transactionType.isEmpty() -> {
                            this.etTransactionType.error = "Transaction type must note be empty"
                        }
                        tag.isEmpty() -> {
                            this.etTag.error = "Tag must note be empty"
                        }
                        date.isEmpty() -> {
                            this.etWhen.error = "Date must note be empty"
                        }
                        note.isEmpty() -> {
                            this.etNote.error = "Note must note be empty"
                        }
                        else -> {
                            viewModel.insertTransaction(getTransactionContent()).also {
                                toast(getString(R.string.success_expense_saved)).also {
                                    findNavController().navigate(
                                        R.id.action_addTransactionFragment_to_dashboardFragment
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private fun getTransactionContent(): Transaction = binding.addTransactionLayout.let {
        val title = it.etTitle.text.toString()
        val amount = it.etAmount.text.toString().toDouble()
        val transactionType = it.etTransactionType.text.toString()
        val tag = it.etTag.text.toString()
        val date = it.etWhen.text.toString()
        val note = it.etNote.text.toString()

        return Transaction(title, amount, transactionType, tag, date, note)
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddTransactionBinding.inflate(inflater, container, false)
}
