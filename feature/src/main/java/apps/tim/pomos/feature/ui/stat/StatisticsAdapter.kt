package apps.tim.pomos.feature.ui.stat

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.toDateString
import apps.tim.pomos.feature.ui.DEFAULT_DATE_LONG
import apps.tim.pomos.feature.ui.tasks.data.Statistics
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.components.YAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.statistics_graph_item.*
import kotlinx.android.synthetic.main.statistics_header_item.*
import kotlinx.android.synthetic.main.statistics_list_item.*
import java.text.DecimalFormat


class StatisticsAdapter(private val items: List<StatisticsItem>,
                        private val stats: List<Statistics>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var totalDonePercentage = 0

    companion object {
        private const val HEADER = 0
        private const val DEFAULT = 1
        private const val GRAPH = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when(viewType) {
            HEADER -> DoneViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.statistics_header_item, parent, false))
            GRAPH -> GraphViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.statistics_graph_item, parent, false))
            else -> StatisticsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.statistics_list_item, parent, false))
        }
    }

    override fun getItemCount() = items.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when(holder.itemViewType) {
            HEADER -> {
                holder as DoneViewHolder
                holder.bind(totalDonePercentage)
            }
            DEFAULT -> {
                holder as StatisticsViewHolder
                holder.bind(items[position - 1])
            }
            else -> {
                holder as GraphViewHolder
                holder.bind(stats.asReversed())
            }
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 ->  HEADER
            itemCount - 1 ->  GRAPH
            else -> {
                DEFAULT
            }
        }
    }

    class DoneViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(totalDonePercentage: Int) {
            result.text = "$totalDonePercentage%"
        }
    }

    class StatisticsViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(statisticsItem: StatisticsItem) {
            with(statisticsItem) {
                taskTitle.text = title
                if (isComplete)
                    taskTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                taskPomos.text = pomo.toString()
                val deadlineVal = deadline
                if (deadlineVal != DEFAULT_DATE_LONG) {
                    showDeadline(deadlineVal)
                }
                percentage.text = "$completePercentage%"
                checkBox.isChecked = isComplete
            }
        }

        private fun showDeadline(deadlineVal: Long) {
            taskDeadline.visibility = View.VISIBLE
            taskDeadlineIcon.visibility = View.VISIBLE
            taskDeadline.text = deadlineVal.toDateString()
        }
    }

    class GraphViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind(stats: List<Statistics>) {
            val entries = mutableListOf<Entry>()
            for (i in 0 until stats.size) {
                entries.add(Entry((i + 1).toFloat(), stats[i].completed.toFloat()))
            }
            chart.legend.isEnabled = false
            chart.extraBottomOffset = 8f

            val dataset = LineDataSet(entries, PomoApp.string(R.string.chart_label))
            dataset.color = PomoApp.color(R.color.colorAccent)
            dataset.lineWidth = 4f
            dataset.setCircleColor(PomoApp.color(R.color.colorAccent))
            dataset.circleRadius = 5f
            dataset.circleHoleRadius = 4f
            dataset.valueTextColor = PomoApp.color(R.color.textColor)
            dataset.valueTextSize = 18f
            dataset.axisDependency = YAxis.AxisDependency.LEFT
            dataset.setDrawValues(false)
            val lineData = LineData(dataset)
            chart.data = lineData

            chart.isDragEnabled = false
            chart.setScaleEnabled(false)

            chart.setBackgroundColor(PomoApp.color(R.color.colorPrimary))
            chart.xAxis.setDrawGridLines(false)
            chart.axisLeft.setDrawGridLines(false)
            chart.axisRight.setDrawGridLines(false)
            chart.axisRight.isEnabled = false
            chart.xAxis.position = XAxis.XAxisPosition.BOTTOM

            chart.axisLeft.valueFormatter = PercentFormatter(DecimalFormat("###"))
            chart.xAxis.valueFormatter = AxisDateFormatter(stats)
            chart.xAxis.granularity = 1f

            chart.axisLeft.axisLineWidth = 3f
            chart.xAxis.axisLineWidth = 3f

            chart.axisLeft.axisLineColor = PomoApp.color(R.color.textColor)
            chart.xAxis.axisLineColor = PomoApp.color(R.color.textColor)

            chart.axisLeft.textSize = 14f
            chart.xAxis.textSize = 14f

            chart.axisLeft.textColor = PomoApp.color(R.color.textColor)
            chart.xAxis.textColor = PomoApp.color(R.color.textColor)

            chart.axisLeft.axisMaximum = 100f
            chart.axisLeft.axisMinimum = 0f
            chart.axisLeft.labelCount = 5

            chart.xAxis.axisMinimum = 0.5f
            chart.xAxis.axisMaximum = 4.5f

            chart.description.isEnabled = false

            chart.isHighlightPerDragEnabled = false
            chart.isHighlightPerTapEnabled = false

            chart.invalidate()
        }

    }

}