package apps.tim.pomos.base.ui.stat.adapter

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.base.DEFAULT_DATE_LONG
import apps.tim.pomos.base.R
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.data.entity.Statistics
import apps.tim.pomos.base.toDateString
import apps.tim.pomos.base.ui.stat.viewmodel.StatisticsListItem
import com.github.mikephil.charting.animation.Easing
import com.github.mikephil.charting.components.XAxis
import com.github.mikephil.charting.data.Entry
import com.github.mikephil.charting.data.LineData
import com.github.mikephil.charting.data.LineDataSet
import com.github.mikephil.charting.formatter.PercentFormatter
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.statistics_graph_item.*
import kotlinx.android.synthetic.main.statistics_list_item.*
import kotlinx.android.synthetic.main.statistics_overall_item.*
import java.text.DecimalFormat


class StatisticsAdapter(private val items: List<StatisticsListItem>,
                        private val stats: List<Statistics>)
    : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    var totalDonePercentage = 0

    companion object {
        private const val HEADER = 0
        private const val DEFAULT = 1
        private const val GRAPH = 2
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            HEADER -> DoneViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.statistics_overall_item, parent, false))
            GRAPH -> GraphViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.statistics_graph_item, parent, false))
            else -> StatisticsViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.statistics_list_item, parent, false))
        }
    }

    override fun getItemCount() = items.size + 2

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder.itemViewType) {
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
            0 -> HEADER
            itemCount - 1 -> GRAPH
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

        fun bind(statisticsListItem: StatisticsListItem) {
            with(statisticsListItem) {
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
                entries.add(Entry((i).toFloat(), stats[i].completed.toFloat()))
            }
            chart.legend.isEnabled = false
            chart.extraBottomOffset = 8f
            chart.isDragEnabled = false
            chart.setScaleEnabled(false)
            chart.setBackgroundColor(PomoApp.color(R.color.colorPrimary))
            chart.description.isEnabled = false
            chart.isHighlightPerDragEnabled = false
            chart.isHighlightPerTapEnabled = false
            chart.animateX(300, Easing.EasingOption.EaseInCubic)

            val dataset = LineDataSet(entries, "")
            dataset.apply {
                color = PomoApp.color(R.color.colorAccent)
                lineWidth = 4f
                setCircleColor(PomoApp.color(R.color.colorAccent))
                circleRadius = 5f
                circleHoleRadius = 4f
                valueTextColor = PomoApp.color(R.color.textColor)
                valueTextSize = 18f
                setDrawValues(false)
                mode = LineDataSet.Mode.CUBIC_BEZIER
            }
            val lineData = LineData(dataset)
            chart.data = lineData

            chart.axisRight.setDrawGridLines(false)
            chart.axisRight.isEnabled = false

            chart.xAxis.apply {
                setDrawGridLines(false)
                position = XAxis.XAxisPosition.BOTTOM
                valueFormatter = AxisDateFormatter(stats)
                granularity = 1f
                axisLineWidth = 3f
                axisLineColor = PomoApp.color(R.color.textColor)
                textSize = 16f
                textColor = PomoApp.color(R.color.textColor)
                axisMinimum = -0.5f
                axisMaximum = entries.size.toFloat() - 0.5f
            }

            chart.axisLeft.apply {
                setDrawGridLines(false)
                valueFormatter = PercentFormatter(DecimalFormat("###"))
                axisLineWidth = 3f
                axisLineColor = PomoApp.color(R.color.textColor)
                textSize = 16f
                textColor = PomoApp.color(R.color.textColor)
                axisMaximum = 100f
                axisMinimum = 0f
            }

            chart.invalidate()
        }

    }

}