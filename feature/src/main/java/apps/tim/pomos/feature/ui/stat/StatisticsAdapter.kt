package apps.tim.pomos.feature.ui.stat

import android.graphics.Paint
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.toDateString
import apps.tim.pomos.feature.ui.DEFAULT_DATE_LONG
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.statistics_graph_item.*
import kotlinx.android.synthetic.main.statistics_header_item.*
import kotlinx.android.synthetic.main.statistics_list_item.*


class StatisticsAdapter(private val items: List<StatisticsItem>)
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
                holder.bind()
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

    class GraphViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun bind() {
            graph.text = "GRAPH"
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

}