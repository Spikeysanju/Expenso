package dev.spikeysanju.expensetracker

import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import dev.spikeysanju.expensetracker.databinding.ActivityMainBinding
import dev.spikeysanju.expensetracker.viewmodel.TransactionViewModel
import hide
import kotlinx.android.synthetic.main.activity_main.*
import show

class MainActivity : AppCompatActivity() {
    private lateinit var navHostFragment: NavHostFragment
    private lateinit var appBarConfiguration: AppBarConfiguration
    private val binding = ActivityMainBinding.inflate(layoutInflater)
    private val viewModel: TransactionViewModel by viewModels()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)
        initViews()
        observeFilter()
        observeNavElements(binding, navHostFragment.navController)

    }

    private fun observeFilter() = with(binding) {
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
                            viewModel.transactionFilter.value = "Overall"
                        }
                        1 -> {
                            viewModel.transactionFilter.value = "Income"
                        }
                        2 -> {
                            viewModel.transactionFilter.value = "Expense"
                        }
                    }
                }

            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
                lifecycleScope.launchWhenStarted {
                    viewModel.transactionFilter.value = "Overall"
                }
            }

        }

    }

    private fun observeNavElements(
        binding: ActivityMainBinding,
        navController: NavController
    ) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {

                R.id.dashboardFragment -> {
                    binding.spinner.show()
                    supportActionBar!!.setDisplayShowTitleEnabled(false)
                }
                R.id.addTransactionFragment -> {
                    binding.spinner.hide()
                    supportActionBar!!.setDisplayShowTitleEnabled(true)
                    binding.toolbar.title = "Add Transaction"
                }
                else -> {
                    binding.spinner.show()
                    supportActionBar!!.setDisplayShowTitleEnabled(false)
                }

            }

        }
    }

    private fun initViews() {
        setSupportActionBar(binding.toolbar)
        supportActionBar!!.setDisplayShowTitleEnabled(false)

        navHostFragment =
            supportFragmentManager.findFragmentById(R.id.nav_host_fragment) as NavHostFragment?
                ?: return

        with(navHostFragment.navController) {
            appBarConfiguration = AppBarConfiguration(graph)
            setupActionBarWithNavController(this, appBarConfiguration)
        }

        // setup filters
        val adapter = ArrayAdapter(
            applicationContext, R.layout.item_filter_dropdown, resources.getStringArray(
                R.array.allFilters
            )
        )
        spinner.adapter = adapter
    }


    override fun onSupportNavigateUp(): Boolean {
        navHostFragment.navController.navigateUp()
        return super.onSupportNavigateUp()
    }
}
