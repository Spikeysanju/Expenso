package dev.spikeysanju.expensetracker.view.dashboard

import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.widget.NestedScrollView
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.FragmentDashboardBinding
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.utils.viewState.ViewState
import dev.spikeysanju.expensetracker.view.adapter.TransactionAdapter
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel
import hide
import indianRupee
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.first
import show
import kotlin.math.abs

@AndroidEntryPoint
class DashboardFragment :
    BaseFragment<FragmentDashboardBinding, TransactionViewModel>() {
    private lateinit var transactionAdapter: TransactionAdapter
    override val viewModel: TransactionViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRV()
        initViews()
        observeFilter()
        observeTransaction()
        swipeToDelete()
    }

    private fun observeFilter() = with(binding) {
        lifecycleScope.launchWhenCreated {
            viewModel.transactionFilter.collect { filter ->
                when (filter) {
                    "Overall" -> {
                        totalBalanceView.totalBalanceTitle.text =
                            getString(R.string.text_total_balance)
                        totalIncomeExpenseView.show()
                        incomeCardView.totalTitle.text = getString(R.string.text_total_income)
                        expenseCardView.totalTitle.text = getString(R.string.text_total_expense)
                        expenseCardView.totalIcon.setImageResource(R.drawable.ic_expense)
                    }
                    "Income" -> {
                        totalBalanceView.totalBalanceTitle.text =
                            getString(R.string.text_total_income)
                        totalIncomeExpenseView.hide()
                    }
                    "Expense" -> {
                        totalBalanceView.totalBalanceTitle.text =
                            getString(R.string.text_total_expense)
                        totalIncomeExpenseView.hide()
                    }
                }
                viewModel.getAllTransaction(filter)
            }
        }
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

    private fun onTotalTransactionLoaded(transaction: List<Transaction>) = with(binding) {
        val (totalIncome, totalExpense) = transaction.partition { it.transactionType == "Income" }
        val income = totalIncome.sumByDouble { it.amount }
        val expense = totalExpense.sumByDouble { it.amount }
        incomeCardView.total.text = "+ ".plus(indianRupee(income))
        expenseCardView.total.text = "- ".plus(indianRupee(expense))
        totalBalanceView.totalBalance.text = indianRupee(income - expense)
    }

    private fun observeTransaction() = lifecycleScope.launchWhenStarted {
        viewModel.uiState.collect { uiState ->
            when (uiState) {
                is ViewState.Loading -> {
                }
                is ViewState.Success -> {
                    showAllViews()
                    onTransactionLoaded(uiState.transaction)
                    onTotalTransactionLoaded(uiState.transaction)
                }
                is ViewState.Error -> {
                    toast("Error")
                }
                is ViewState.Empty -> {
                    hideAllViews()
                }
            }
        }
    }

    private fun showAllViews() = with(binding) {
        dashboardGroup.show()
        emptyStateLayout.hide()
        transactionRv.show()
    }

    private fun hideAllViews() = with(binding) {
        dashboardGroup.hide()
        emptyStateLayout.show()
    }

    private fun onTransactionLoaded(list: List<Transaction>) =
        transactionAdapter.differ.submitList(list)

    private fun initViews() = with(binding) {
        btnAddTransaction.setOnClickListener {
            findNavController().navigate(R.id.action_dashboardFragment_to_addTransactionFragment)
        }

        mainDashboardScrollView.setOnScrollChangeListener(
            NestedScrollView.OnScrollChangeListener { _, sX, sY, oX, oY ->
                if (abs(sY - oY) > 10) {
                    when {
                        sY > oY -> btnAddTransaction.hide()
                        oY > sY -> btnAddTransaction.show()
                    }
                }
            }
        )

        transactionAdapter.setOnItemClickListener {
            val bundle = Bundle().apply {
                putSerializable("transaction", it)
            }
            findNavController().navigate(
                R.id.action_dashboardFragment_to_transactionDetailsFragment,
                bundle
            )
        }
    }

    override fun getViewBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ) = FragmentDashboardBinding.inflate(inflater, container, false)

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_ui, menu)

        val item = menu.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner

        val adapter = ArrayAdapter.createFromResource(
            applicationContext(),
            R.array.allFilters,
            R.layout.item_filter_dropdown
        )
        adapter.setDropDownViewResource(R.layout.item_filter_dropdown)
        spinner.adapter = adapter

        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                lifecycleScope.launchWhenStarted {
                    when (position) {
                        0 -> {
                            viewModel.overall()
                            (view as TextView).setTextColor(resources.getColor(R.color.black))
                        }
                        1 -> {
                            viewModel.allIncome()
                            (view as TextView).setTextColor(resources.getColor(R.color.black))
                        }
                        2 -> {
                            viewModel.allExpense()
                            (view as TextView).setTextColor(resources.getColor(R.color.black))
                        }
                    }
                }
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                lifecycleScope.launchWhenStarted {
                    viewModel.overall()
                }
            }
        }

        // Set the item state
        lifecycleScope.launchWhenStarted {
            val isChecked = viewModel.getUIMode.first()
            val uiMode = menu.findItem(R.id.action_night_mode)
            uiMode.isChecked = isChecked
            setUIMode(uiMode, isChecked)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here.
        return when (item.itemId) {
            R.id.action_night_mode -> {
                item.isChecked = !item.isChecked
                setUIMode(item, item.isChecked)
                true
            }

            R.id.action_about -> {
                findNavController().navigate(R.id.action_dashboardFragment_to_aboutFragment)
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    private fun setUIMode(item: MenuItem, isChecked: Boolean) {
        if (isChecked) {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
            viewModel.saveToDataStore(true)
            item.setIcon(R.drawable.ic_night)
        } else {
            AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
            viewModel.saveToDataStore(false)
            item.setIcon(R.drawable.ic_day)
        }
    }
}
