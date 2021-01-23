package dev.spikeysanju.expensetracker.view

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.FragmentDashboardBinding
import dev.spikeysanju.expensetracker.db.AppDatabase
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.repo.TransactionRepo
import dev.spikeysanju.expensetracker.utils.ViewState
import dev.spikeysanju.expensetracker.utils.viewModelFactory
import dev.spikeysanju.expensetracker.view.adapter.TransactionAdapter
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import dev.spikeysanju.expensetracker.viewmodel.TransactionViewModel
import indianRupee
import kotlinx.coroutines.flow.collect

class DashboardFragment :
    BaseFragment<FragmentDashboardBinding, TransactionViewModel>() {
    private lateinit var transactionAdapter: TransactionAdapter
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
        setupRV()
        observeDashboardTotal()
        swipeToDelete()
    }

    private fun setupRV() = with(binding) {
        transactionAdapter = TransactionAdapter()
        transactionRv.apply {
            adapter = transactionAdapter
            layoutManager = LinearLayoutManager(activity)
        }
    }

    private fun swipeToDelete() {
        // init item touch callback for swipe action
        val itemTouchHelperCallback = object : ItemTouchHelper.SimpleCallback(
            ItemTouchHelper.UP or ItemTouchHelper.DOWN,
            ItemTouchHelper.LEFT or ItemTouchHelper.RIGHT
        ) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder,
            ): Boolean {
                return true
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                // get item position & delete notes
                val position = viewHolder.adapterPosition
                val transaction = transactionAdapter.differ.currentList[position]
                val transactionItem = Transaction(
                    transaction.title,
                    transaction.amount,
                    transaction.transactionType,
                    transaction.tag,
                    transaction.date,
                    transaction.note,
                    transaction.createdAt,
                    transaction.id
                )
                viewModel.deleteTransaction(transactionItem)
                Snackbar.make(
                    binding.root,
                    getString(R.string.success_transaction_delete),
                    Snackbar.LENGTH_LONG
                )
                    .apply {
                        setAction("Undo") {
                            viewModel.insertTransaction(
                                transactionItem
                            )
                        }
                        show()
                    }
            }
        }

        // attach swipe callback to rv
        ItemTouchHelper(itemTouchHelperCallback).apply {
            attachToRecyclerView(binding.transactionRv)
        }
    }

    private fun observeDashboardTotal() = lifecycleScope.launchWhenStarted {
        viewModel.uiState.collect { uiState ->
            when (uiState) {
                ViewState.Loading -> TODO()
                ViewState.Empty -> TODO()
                is ViewState.Success -> {
                    onTotalTransactionLoaded(uiState.transaction)
                }
                is ViewState.Error -> TODO()
            }
        }
    }

    private fun onTotalTransactionLoaded(transaction: List<Transaction>) = with(binding) {
        val (totalIncome, totalExpense) = transaction.partition { it.transactionType == "Income" }
        val income = totalIncome.sumByDouble { it.amount }
        val expense = totalExpense.sumByDouble { it.amount }
        incomeCardView.total.text = indianRupee(income)
        expenseCardView.total.text = indianRupee(expense)
        totalBalanceView.totalBalance.text = indianRupee(income - expense)

    }

    private fun observeData() {

        lifecycleScope.launchWhenStarted {
            viewModel.uiState.collect { uiState ->
                when (uiState) {
                    is ViewState.Loading -> {
                    }
                    is ViewState.Success -> {
                        onTransactionLoaded(uiState.transaction)
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

    private fun onTransactionLoaded(list: List<Transaction>) {
        transactionAdapter.differ.submitList(list)
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
