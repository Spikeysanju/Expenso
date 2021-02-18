package dev.spikeysanju.expensetracker.view.statistics

import android.graphics.Color
import androidx.lifecycle.ViewModelProvider
import android.os.Bundle
import android.view.*
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Spinner
import android.widget.TextView
import androidx.appcompat.app.AppCompatDelegate
import androidx.fragment.app.Fragment

import androidx.core.view.size
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.viewModelScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.Legend
import com.github.mikephil.charting.data.PieData
import com.github.mikephil.charting.data.PieDataSet
import com.github.mikephil.charting.data.PieEntry
import com.github.mikephil.charting.utils.ColorTemplate
import dagger.hilt.android.AndroidEntryPoint
import dev.spikeysanju.expensetracker.R
import dev.spikeysanju.expensetracker.databinding.StatisticsFragmentBinding
import dev.spikeysanju.expensetracker.model.Transaction
import dev.spikeysanju.expensetracker.utils.viewModelFactory
import dev.spikeysanju.expensetracker.utils.viewState.ViewState
import dev.spikeysanju.expensetracker.view.base.BaseFragment
import dev.spikeysanju.expensetracker.view.details.TransactionDetailsFragmentArgs
import dev.spikeysanju.expensetracker.view.main.viewmodel.TransactionViewModel
import kotlinx.android.synthetic.main.statistics_fragment.*
import kotlinx.coroutines.flow.collect
import kotlin.math.exp
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.coroutines.flow.first
import kotlin.properties.Delegates


@AndroidEntryPoint
class StatisticsFragment : BaseFragment<StatisticsFragmentBinding, TransactionViewModel>() {


    private val args: StatisticsFragmentArgs by navArgs()
    override val viewModel: TransactionViewModel by activityViewModels()
    private lateinit var filter :String

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setHasOptionsMenu(true)

        observeFilter()
        observeTransaction()

    }

    private fun observeTransaction() = lifecycleScope.launchWhenStarted {
        viewModel.uiState.collect { uiState ->
            when (uiState) {
                is ViewState.Loading -> {
                }
                is ViewState.Success -> {

                    onTotalTransactionLoaded(uiState.transaction)
                }
                is ViewState.Error -> {
                    toast("Error")
                }
                is ViewState.Empty -> {

                }
            }
        }
    }


    private fun onTotalTransactionLoaded(transaction: List<Transaction>) = generateChartForFilter(
        transaction,

    )



    private fun observeFilter() = with(binding) {
        lifecycleScope.launchWhenCreated {
            viewModel.transactionFilter.collect { filter ->
                viewModel.getAllTransaction(filter)
                 this@StatisticsFragment.filter = filter
            }
        }
    }



    private fun generateChartForFilter(transactions: List<Transaction>) {
        println("generateChart")
        val (totalIncome, totalExpense) = transactions.partition { it.transactionType == "Income" }
        val income = totalIncome.sumByDouble { it.amount }
        val expense = totalExpense.sumByDouble { it.amount }
        val entriesForPieChart : ArrayList<PieEntry> = ArrayList()
        var centerText = ""

        when(filter){
            "Overall" -> {

                val totalBalance = income + expense
                val incomePercentage = ((totalBalance - expense) / totalBalance * 100).toFloat()
                val expensePercentage = ((totalBalance - income) / totalBalance * 100).toFloat()
                entriesForPieChart.apply {
                    add(PieEntry(incomePercentage, "Income"))
                    add(PieEntry(expensePercentage, "Expense"))
                }

                centerText = "Overall Balance \n ${income - expense}"
            }
            "Income" -> {
                totalIncome.forEach {
                    val tempPercentage = (it.amount / income * 100).toFloat()
                    entriesForPieChart.add(PieEntry(tempPercentage, it.title.capitalize()))
                }
                centerText = " Total Income: \n ${income}"
            }
            "Expense" -> {
                totalExpense.forEach {
                    val tempPercentage = (it.amount / expense * 100).toFloat()
                    entriesForPieChart.add(PieEntry(tempPercentage, it.title.capitalize()))
                }
                centerText = " Total Expense: \n ${expense}"
            }
        }

        val l: Legend =  pie_chart.getLegend()
        l.setVerticalAlignment(Legend.LegendVerticalAlignment.TOP)
        l.setHorizontalAlignment(Legend.LegendHorizontalAlignment.RIGHT)
        l.setOrientation(Legend.LegendOrientation.VERTICAL)
        l.setXEntrySpace(7f)
        l.setYEntrySpace(0f)
        l.setYOffset(0f)



        val pieSet = PieDataSet(entriesForPieChart, filter)
        // add a lot of colors

        val colors: ArrayList<Int> = ArrayList()

        for (c in ColorTemplate.VORDIPLOM_COLORS) colors.add(c)

        for (c in ColorTemplate.JOYFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.COLORFUL_COLORS) colors.add(c)

        for (c in ColorTemplate.LIBERTY_COLORS) colors.add(c)

        for (c in ColorTemplate.PASTEL_COLORS) colors.add(c)

        colors.add(ColorTemplate.getHoloBlue())

        pieSet.setColors(colors)

        pieSet.setValueLinePart1OffsetPercentage(80.0f);
        pieSet.setValueLinePart1Length(0.2f);
        pieSet.setValueLinePart2Length(0.4f);

        pieSet.setYValuePosition(PieDataSet.ValuePosition.OUTSIDE_SLICE);





        val data = PieData(pieSet)

        data.setValueFormatter(PercentFormatter())
        data.setValueTextSize(11f)

        lifecycleScope.launchWhenStarted {
            val isItNight = viewModel.getUIMode.first()
            if(isItNight == true)
                data.setValueTextColor(Color.WHITE)
            else
                data.setValueTextColor(Color.BLACK)
        }







        with(binding){

            with(pie_chart){
                setExtraOffsets(20.0f,0.0f,20.0f,0.0f)


                rotationAngle = 0.0f
                isRotationEnabled = true
                isHighlightPerTapEnabled = true
                setEntryLabelColor(Color.BLACK)
                description.text = "${filter?.capitalize()} ".toString()
                this.setEntryLabelTextSize(12f)
                this.centerText = centerText
                this.setCenterTextSize(16f)
                animateY(1400, Easing.EaseInOutQuad)
                this.data = data
                invalidate()
            }

        }
    }

    override fun getViewBinding(inflater: LayoutInflater, container: ViewGroup?): StatisticsFragmentBinding {
       return StatisticsFragmentBinding.inflate(inflater, container, false)
    }



    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_ui, menu)

        val item = menu.findItem(R.id.spinner)
        val spinner = item.actionView as Spinner

        val adapter = ArrayAdapter.createFromResource(
            applicationContext(),
            R.array.allFilters,
            R.layout.item_filter_dropdown
        )

        val filterArray = resources.getStringArray(R.array.allFilters)

        adapter.setDropDownViewResource(R.layout.item_filter_dropdown)
        spinner.adapter = adapter

        var adapterFilter = "Overall"
        if(filter!="Overall")
            adapterFilter = "All "+filter


        spinner.setSelection(adapter.getPosition(adapterFilter.toString()))
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
