package com.agomezlucena.youcandoit.task_managament.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.agomezlucena.youcandoit.*
import com.agomezlucena.youcandoit.databinding.FragmentStatisticsBinding
import com.agomezlucena.youcandoit.task_managament.DayScore
import com.agomezlucena.youcandoit.task_managament.MonthScore
import com.agomezlucena.youcandoit.task_managament.ScoreData
import com.agomezlucena.youcandoit.task_managament.ui.statistics.viewmodels.ThisYearStatisticsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.time.Month

@AndroidEntryPoint
class ThisYearStatistics : Fragment() {
    private lateinit var binding: FragmentStatisticsBinding
    private val viewmodel by viewModels<ThisYearStatisticsViewModel>()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        binding.barchartTitle.text = getString(R.string.this_year_statistics_bar_chart_title)
        binding.piechartTitle.text = getString(R.string.this_year_pie_chart_title)
        setupBarChart(binding.barChart)
        setUpPieChart(binding.pieChart)
        viewmodel.getYearScoreGroupByDay().observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty()) populateBarChar(binding.barChart, it)
            }

        }
        populatePieChart(binding.pieChart)
        return binding.root
    }

    private fun setupBarChart(barChart: BarChart) {
        barChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }

        barChart.axisLeft.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }

        barChart.axisRight.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }

        barChart.legend.isEnabled = false
        barChart.description.isEnabled = false

        val xAxisFormatter = IndexAxisValueFormatter().apply {
            values = Month.values().map { translateMonthAbbreviation(requireContext(), it) }
                .toTypedArray()
        }

        barChart.xAxis.valueFormatter = xAxisFormatter
        barChart.animateY(1000)
        barChart.animateX(1000)
    }

    private fun setUpPieChart(pieChart: PieChart) {
        pieChart.apply {
            setHoleColor(Color.TRANSPARENT)
            setEntryLabelColor(Color.BLACK)
            animateY(1000)
            animateX(1000)
            setUsePercentValues(true)
            description.text = ""
        }
    }

    private fun populateBarChar(barChart: BarChart, list: List<DayScore>) {
        launchCoroutine(Dispatchers.IO) {
            val monthScores = list
                .groupBy { it.executionDate.month }
                .map {
                    val monthScore = it.value.map { dayScore ->
                        dayScore.score
                    }.sum()
                    MonthScore(it.key, monthScore)
                }

            val barDataSets = Month.values().mapIndexed { index, month ->
                val monthScore =
                    monthScores.find { ms -> ms.month == month }?.score?.toFloat() ?: 0.1f
                val entry = BarEntry(index.toFloat(), monthScore)
                BarDataSet(listOf(entry), translateMonth(requireContext(), month)).apply {
                    valueTextColor = Color.TRANSPARENT
                    color = Color.parseColor("#3c74a0")
                }
            }

            val barData = BarData(barDataSets)
            launchCoroutine(Dispatchers.Main) {
                barChart.data = barData
                barChart.invalidate()
            }
        }
    }

    private fun populatePieChart(pieChart: PieChart) {
        launchCoroutine(Dispatchers.IO) {
            val scoreData = viewmodel.thisYearScore()
            if(scoreData.totalScore > 0){
                Timber.d(scoreData.toString())
                val percentageAccomplished =
                    ((scoreData.accomplishedScore * 100) / scoreData.totalScore).toFloat()

                val percentageOfRemainderScore =
                    ((scoreData.totalScore - scoreData.accomplishedScore) * 100 / scoreData.totalScore).toFloat()


                val pieData = PieData().apply {
                    dataSet = PieDataSet(
                        listOf(
                            PieEntry(
                                percentageAccomplished,
                                getString(R.string.accomplished_percentage)
                            ),
                            PieEntry(
                                percentageOfRemainderScore,
                                getString(R.string.remainder_percentage)
                            )
                        ), ""
                    ).apply {
                        sliceSpace = 2.0f
                        val colors = arrayOf(R.color.chart_blue, R.color.chart_red)
                        setColors(colors.toIntArray(), requireContext())
                        valueTextColor = Color.BLACK
                    }
                }
                launchCoroutine(Dispatchers.Main) {
                    pieChart.data = pieData
                    pieChart.invalidate()
                }
            }

        }
    }
}