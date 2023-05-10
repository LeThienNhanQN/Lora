package com.ldnhat.loraapp.ui.fragment.chart

import android.content.Context
import android.graphics.Color
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.interfaces.datasets.ILineDataSet
import com.ldnhat.loraapp.R
import com.ldnhat.loraapp.common.model.Data
import com.ldnhat.loraapp.databinding.FragmentChartBinding
import com.ldnhat.loraapp.utils.constants.Constants


class ChartFragment : Fragment() {
    internal var data: List<Data> = ArrayList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding: FragmentChartBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_chart, container, false)

        val sharedPref =
            requireActivity().getSharedPreferences(Constants.TOKEN, Context.MODE_PRIVATE)
        val accessToken: String? = sharedPref.getString(Constants.ACCESS_TOKEN, null)


        val application = requireActivity().application
        val chartViewModelFactory = ChartViewModelFactory(application, accessToken.toString())
        val chartViewModel =
            ViewModelProvider(this, chartViewModelFactory).get(ChartViewModel::class.java)
        binding.chartViewModel = chartViewModel

        chartViewModel.data.observe(viewLifecycleOwner, {
            if (null != it) {
                data = it.filter { s ->
                            isNumeric(s.humidity) &&
                            isNumeric(s.temperature) &&
                            isNumeric(s.lightIntensity) &&
                            isNumeric(s.rssiIntensity) &&
                            isNumeric(s.soilMoisture)
                }

                generateTemperatureChart(data, binding)
                generateHumidityChart(data, binding)
                generateLightChart(data, binding)
                generateRssiChart(data, binding)
                generateSoilChart(data, binding)
            }
        })


        binding.lifecycleOwner = this
        return binding.root
    }

    private fun generateTemperatureChart(data: List<Data>, binding: FragmentChartBinding) {
        val temperatureChart = binding.temperatureChart
        temperatureChart.description.isEnabled = false
        temperatureChart.setDrawGridBackground(false)
        temperatureChart.setDrawBarShadow(false)
        temperatureChart.isHighlightFullBarEnabled = false

        val rightAxis: YAxis = temperatureChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = -100f

        val leftAxis: YAxis = temperatureChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = -100f

        val xAxis: XAxis = temperatureChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.mAxisMinimum = 0f
        xAxis.granularity = 1f

        val combinedData = CombinedData()
        val lineData = LineData()
        lineData.addDataSet(dataTemperatureChart(data) as ILineDataSet)
        combinedData.setData(lineData)
        xAxis.axisMaximum = combinedData.xMax + 0.25f
        temperatureChart.data = combinedData
        temperatureChart.invalidate()
    }

    private fun dataTemperatureChart(data: List<Data>): DataSet<Entry> {
        val lineData = LineData()

        val entries = ArrayList<Entry>()
        for (i in data.indices) {
            entries.add(Entry(i.toFloat(), (data[i].temperature).toFloat()))
        }

        val set = LineDataSet(entries, "Temperature data")
        set.color = Color.RED
        set.lineWidth = 2.5f
        set.setCircleColor(Color.RED)
        set.circleRadius = 2f
        set.fillColor = Color.RED
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 5f
        set.valueTextColor = Color.RED

        set.axisDependency = YAxis.AxisDependency.LEFT

        set.axisDependency = YAxis.AxisDependency.LEFT
        lineData.addDataSet(set)

        return set
    }

    private fun generateHumidityChart(data: List<Data>, binding: FragmentChartBinding) {
        val humidityChart = binding.humidityChart
        humidityChart.description.isEnabled = false
        humidityChart.setDrawGridBackground(false)
        humidityChart.setDrawBarShadow(false)
        humidityChart.isHighlightFullBarEnabled = false

        val rightAxis: YAxis = humidityChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = -100f

        val leftAxis: YAxis = humidityChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = -100f

        val xAxis: XAxis = humidityChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.mAxisMinimum = 0f
        xAxis.granularity = 1f

        val combinedData = CombinedData()
        val lineData = LineData()
        lineData.addDataSet(dataHumidityChart(data) as ILineDataSet)
        combinedData.setData(lineData)
        xAxis.axisMaximum = combinedData.xMax + 0.25f
        humidityChart.data = combinedData
        humidityChart.invalidate()
    }

    private fun dataHumidityChart(data: List<Data>): DataSet<Entry> {
        val lineData = LineData()

        val entries = ArrayList<Entry>()
        for (i in data.indices) {
            entries.add(Entry(i.toFloat(), (data[i].humidity).toFloat()))
        }

        val set = LineDataSet(entries, "Humidity data")
        set.color = Color.BLUE
        set.lineWidth = 2.5f
        set.setCircleColor(Color.BLUE)
        set.circleRadius = 2f
        set.fillColor = Color.BLUE
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 5f
        set.valueTextColor = Color.BLUE

        set.axisDependency = YAxis.AxisDependency.LEFT

        set.axisDependency = YAxis.AxisDependency.LEFT
        lineData.addDataSet(set)

        return set
    }

    private fun generateRssiChart(data: List<Data>, binding: FragmentChartBinding) {
        val rssiChart = binding.rssiChart
        rssiChart.description.isEnabled = false
        rssiChart.setDrawGridBackground(false)
        rssiChart.setDrawBarShadow(false)
        rssiChart.isHighlightFullBarEnabled = false

        val rightAxis: YAxis = rssiChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = -100f

        val leftAxis: YAxis = rssiChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = -100f

        val xAxis: XAxis = rssiChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.mAxisMinimum = 0f
        xAxis.granularity = 1f

        val combinedData = CombinedData()
        val lineData = LineData()
        lineData.addDataSet(dataRssiChart(data) as ILineDataSet)
        combinedData.setData(lineData)
        xAxis.axisMaximum = combinedData.xMax + 0.25f
        rssiChart.data = combinedData
        rssiChart.invalidate()
    }

    private fun dataRssiChart(data: List<Data>): DataSet<Entry> {
        val lineData = LineData()

        val entries = ArrayList<Entry>()
        for (i in data.indices) {
            entries.add(Entry(i.toFloat(), (data[i].rssiIntensity).toFloat()))
        }

        val set = LineDataSet(entries, "Rssi Intensity data")
        set.color = Color.BLACK
        set.lineWidth = 2.5f
        set.setCircleColor(Color.BLACK)
        set.circleRadius = 2f
        set.fillColor = Color.BLACK
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 5f
        set.valueTextColor = Color.BLACK

        set.axisDependency = YAxis.AxisDependency.LEFT

        set.axisDependency = YAxis.AxisDependency.LEFT
        lineData.addDataSet(set)

        return set
    }

    private fun generateLightChart(data: List<Data>, binding: FragmentChartBinding) {
        val lightChart = binding.lightChart
        lightChart.description.isEnabled = false
        lightChart.setDrawGridBackground(false)
        lightChart.setDrawBarShadow(false)
        lightChart.isHighlightFullBarEnabled = false

        val rightAxis: YAxis = lightChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = -100f

        val leftAxis: YAxis = lightChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = -100f

        val xAxis: XAxis = lightChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.mAxisMinimum = 0f
        xAxis.granularity = 1f

        val combinedData = CombinedData()
        val lineData = LineData()
        lineData.addDataSet(dataLightChart(data) as ILineDataSet)
        combinedData.setData(lineData)
        xAxis.axisMaximum = combinedData.xMax + 0.25f
        lightChart.data = combinedData
        lightChart.invalidate()
    }

    private fun dataLightChart(data: List<Data>): DataSet<Entry> {
        val lineData = LineData()

        val entries = ArrayList<Entry>()
        for (i in data.indices) {
            entries.add(Entry(i.toFloat(), (data[i].lightIntensity).toFloat()))
        }

        val set = LineDataSet(entries, "Light Intensity data")
        set.color = Color.YELLOW
        set.lineWidth = 2.5f
        set.setCircleColor(Color.YELLOW)
        set.circleRadius = 2f
        set.fillColor = Color.YELLOW
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 5f
        set.valueTextColor = Color.BLACK

        set.axisDependency = YAxis.AxisDependency.LEFT

        set.axisDependency = YAxis.AxisDependency.LEFT
        lineData.addDataSet(set)

        return set
    }

    private fun generateSoilChart(data: List<Data>, binding: FragmentChartBinding) {
        val soilChart = binding.soilChart
        soilChart.description.isEnabled = false
        soilChart.setDrawGridBackground(false)
        soilChart.setDrawBarShadow(false)
        soilChart.isHighlightFullBarEnabled = false

        val rightAxis: YAxis = soilChart.axisRight
        rightAxis.setDrawGridLines(false)
        rightAxis.axisMinimum = -100f

        val leftAxis: YAxis = soilChart.axisLeft
        leftAxis.setDrawGridLines(false)
        leftAxis.axisMinimum = -100f

        val xAxis: XAxis = soilChart.xAxis
        xAxis.position = XAxis.XAxisPosition.BOTTOM
        xAxis.mAxisMinimum = 0f
        xAxis.granularity = 1f

        val combinedData = CombinedData()
        val lineData = LineData()
        lineData.addDataSet(dataSoilChart(data) as ILineDataSet)
        combinedData.setData(lineData)
        xAxis.axisMaximum = combinedData.xMax + 0.25f
        soilChart.data = combinedData
        soilChart.invalidate()
    }

    private fun dataSoilChart(data: List<Data>): DataSet<Entry> {
        val lineData = LineData()

        val entries = ArrayList<Entry>()
        for (i in data.indices) {
            entries.add(Entry(i.toFloat(), (data[i].soilMoisture).toFloat()))
        }

        val set = LineDataSet(entries, "Soil Moisture data")
        set.color = Color.BLACK
        set.lineWidth = 2.5f
        set.setCircleColor(Color.BLACK)
        set.circleRadius = 2f
        set.fillColor = Color.BLACK
        set.mode = LineDataSet.Mode.CUBIC_BEZIER
        set.setDrawValues(true)
        set.valueTextSize = 5f
        set.valueTextColor = Color.BLACK

        set.axisDependency = YAxis.AxisDependency.LEFT

        set.axisDependency = YAxis.AxisDependency.LEFT
        lineData.addDataSet(set)

        return set
    }

    private fun isNumeric(number: String): Boolean {
        try {
            val d: Float = number.toFloat()
        } catch (nfe: NumberFormatException) {
            return false
        }
        return true
    }
}