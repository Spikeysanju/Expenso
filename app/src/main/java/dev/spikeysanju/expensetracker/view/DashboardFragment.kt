package dev.spikeysanju.expensetracker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.FragmentDashboardBinding
import dev.spikeysanju.expensetracker.db.AppDatabase
import dev.spikeysanju.expensetracker.repo.TransactionRepo
import dev.spikeysanju.expensetracker.utils.ViewState
import dev.spikeysanju.expensetracker.utils.viewModelFactory
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import dev.spikeysanju.expensetracker.viewmodel.TransactionViewModel
import kotlinx.coroutines.flow.collect

class DashboardFragment :
    BaseFragment<FragmentDashboardBinding, TransactionViewModel>() {

    private val transactionRepo by lazy {
        TransactionRepo(AppDatabase.invoke(applicationContext()))
    }
    override val viewModel: TransactionViewModel by viewModels {
        viewModelFactory { TransactionViewModel(requireActivity().application, transactionRepo) }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initViews()
        observeData()
    }

    private fun observeData() {

        lifecycleScope.launchWhenCreated {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is ViewState.Loading -> {
                    }
                    is ViewState.Success -> {
                        toast("Transaction is ${uiState.transaction.first().title}")
                    }
                    is ViewState.Error -> {
                        toast("Error")
                    }
                    is ViewState.Empty -> {
                        toast("Empty")
                    }
                }
            }
        }
    }

    private fun initViews() = with(binding) {
        btnAddTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addTransactionFragment)
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)
}
