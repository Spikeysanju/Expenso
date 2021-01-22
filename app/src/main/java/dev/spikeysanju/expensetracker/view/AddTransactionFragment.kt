package dev.spikeysanju.expensetracker.view


import android.view.LayoutInflater
import android.view.ViewGroup
import dev.spikeysanju.expensetracker.databinding.FragmentAddTransactionBinding
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import dev.spikeysanju.expensetracker.viewmodel.TransactionViewModel

class AddTransactionFragment : BaseFragment<FragmentAddTransactionBinding, TransactionViewModel>() {
    override val viewModel: TransactionViewModel
        get() = TODO("Not yet implemented")

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentAddTransactionBinding.inflate(inflater, container, false)

}
