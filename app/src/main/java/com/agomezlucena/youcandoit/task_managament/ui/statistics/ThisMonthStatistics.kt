package com.agomezlucena.youcandoit.task_managament.ui.statistics

import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.agomezlucena.youcandoit.R
import com.agomezlucena.youcandoit.databinding.FragmentMonthStatisticsBinding
import com.agomezlucena.youcandoit.launchCoroutine
import com.agomezlucena.youcandoit.task_managament.DayScore
import com.agomezlucena.youcandoit.task_managament.ui.statistics.viewmodels.ThisMonthStatisticsViewModel
import com.github.mikephil.charting.charts.LineChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import timber.log.Timber
import java.time.Duration
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters

@AndroidEntryPoint
class ThisMonthStatistics : Fragment() {
    private lateinit var binding: FragmentMonthStatisticsBinding
    private val viewModel by viewModels<ThisMonthStatisticsViewModel>()
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentMonthStatisticsBinding.inflate(layoutInflater, container, false)
        setUpLineChart(binding.lineChart)
        setUpPieChart(binding.pieChart)
        binding.lineChartTitle.text = getString(R.string.this_month_statistics_linechart_title)
        binding.piechartTitle.text = getString(R.string.this_month_pie_chart_title)


        viewModel.getMonthScoreGroupByDay().observe(viewLifecycleOwner) {
            it?.let {
                if(it.isNotEmpty())  populateLineChart(binding.lineChart, it)
            }
        }

        populatePieChart(binding.pieChart)
        return binding.root
    }

    private fun setUpLineChart(lineChart: LineChart) {
        lineChart.xAxis.apply {
            position = XAxis.XAxisPosition.BOTTOM
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }

        lineChart.axisLeft.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }

        lineChart.axisRight.apply {
            axisLineColor = Color.BLACK
            textColor = Color.BLACK
            setDrawGridLines(false)
        }

        lineChart.apply {
            legend.isEnabled = false
            description.isEnabled = false
            animateX(1000)
            animateY(1000)
        }
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

    private fun populateLineChart(lineChart: LineChart, list: List<DayScore>) {
        launchCoroutine(Dispatchers.IO) {
            val firstDayOfThisMonth = LocalDate.now().with(TemporalAdjusters.firstDayOfMonth())
            val lastDayOfMonth = LocalDate.now().with(TemporalAdjusters.lastDayOfMonth())
            val daysBetween =
                Duration.between(firstDayOfThisMonth.atStartOfDay(), lastDayOfMonth.atStartOfDay())
                    .toDays()

            val entrys = (0..daysBetween).mapIndexed { index, daysToAdd ->
                val scoreFromDay =
                    list.find { it.executionDate == firstDayOfThisMonth.plusDays(daysToAdd) }?.score?.toFloat()
                        ?: 0.0f
                Entry(index.toFloat(), scoreFromDay)
            }

            val lineData = LineData().apply {
                addDataSet(LineDataSet(entrys, "Puntuación conseguida por día este mes").apply {
                    valueTextColor = Color.TRANSPARENT
                    color = Color.parseColor("#3c74a0")
                })
            }

            launchCoroutine(Dispatchers.Main) {
                lineChart.data = lineData
                lineChart.invalidate()
            }
        }
    }

    private fun populatePieChart(pieChart: PieChart) {
        launchCoroutine(Dispatchers.IO) {
            val scoreData = viewModel.thisMonthScore()
            if (scoreData.totalScore > 0){
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