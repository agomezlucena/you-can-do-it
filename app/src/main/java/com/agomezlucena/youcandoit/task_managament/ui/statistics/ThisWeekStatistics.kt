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
import com.agomezlucena.youcandoit.task_managament.ScoreData
import com.agomezlucena.youcandoit.task_managament.ui.statistics.viewmodels.ThisWeekStatisticsViewModel
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.charts.PieChart
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import timber.log.Timber
import java.time.DayOfWeek

@AndroidEntryPoint
class ThisWeekStatistics : Fragment() {
    private lateinit var binding: FragmentStatisticsBinding
    private val viewModel: ThisWeekStatisticsViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentStatisticsBinding.inflate(inflater, container, false)
        binding.barchartTitle.setText(R.string.this_week_statistics_barchart_title)
        binding.piechartTitle.setText(R.string.this_week_pie_chart_title)
        setupBarChart(binding.barChart)
        setUpPieChart(binding.pieChart)
        viewModel.getWeekScoreGroupByDay().observe(viewLifecycleOwner) {
            it?.let {
                if (it.isNotEmpty())
                    setBarChartEntry(binding.barChart, it)
            }
        }

        setPieChartEntry(binding.pieChart)
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
        val xAxisFormatter = IndexAxisValueFormatter()
        xAxisFormatter.values =
            DayOfWeek.values().map { translateDayOfWeekAbbreviation(requireContext(), it) }
                .toTypedArray()

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

    private fun setBarChartEntry(barChart: BarChart, list: List<DayScore>) {
        launchCoroutine(Dispatchers.IO) {
            val barChartDataSets = DayOfWeek.values().mapIndexed { index, dayOfWeek ->
                val entry = BarEntry(
                    index.toFloat(),
                    (list.find { it.executionDate.dayOfWeek == dayOfWeek }?.score ?: .1).toFloat()
                )
                BarDataSet(listOf(entry), translateDayOfWeek(requireContext(), dayOfWeek)).apply {
                    valueTextColor = Color.TRANSPARENT
                    color = Color.parseColor("#3c74a0")
                }
            }

            launchCoroutine(Dispatchers.Main) {
                barChart.data = BarData(barChartDataSets)
                barChart.invalidate()
            }
        }


    }

    private fun setPieChartEntry(pieChart: PieChart) {
        launchCoroutine(Dispatchers.IO) {
            val scoreData = viewModel.thisWeekScore()
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