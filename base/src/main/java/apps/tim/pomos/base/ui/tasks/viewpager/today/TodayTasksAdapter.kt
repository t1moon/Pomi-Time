package apps.tim.pomos.base.ui.tasks.viewpager.today

import android.graphics.Paint
import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.base.DEFAULT_DATE_LONG
import apps.tim.pomos.base.R
import apps.tim.pomos.base.data.entity.Task
import apps.tim.pomos.base.toDateString
import apps.tim.pomos.base.ui.tasks.viewpager.TaskDiffCallback
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.today_list_item.*


open class TodayTasksAdapter(private val itemClick: (Task) -> Unit,
                             private val longItemClick: (Task) -> Boolean,
                             private val onCheckedChange: (Task, Boolean) -> Unit)
    : ListAdapter<Task, RecyclerView.ViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodayTaskHolder(LayoutInflater.from(parent.context).inflate(R.layout.today_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as TodayTaskHolder
        holder.bind(getItem(position), itemClick, longItemClick, onCheckedChange)
    }

    class TodayTaskHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(task: Task,
                 itemClick: (Task) -> Unit,
                 longItemClick: (Task) -> Boolean,
                 onCheckedChange: (Task, Boolean) -> Unit) {
            with(task) {
                taskTitle.text = title
                if (isComplete)
                    taskTitle.paintFlags = Paint.STRIKE_THRU_TEXT_FLAG
                taskPomos.text = currentPomo.toString()
                val deadlineVal = deadline
                if (deadlineVal != DEFAULT_DATE_LONG) {
                    showDeadline(deadlineVal)
                }
                checkBox.isChecked = isComplete
                checkBox.setOnCheckedChangeListener { _, b ->
                    onCheckedChange(this, b)
                }
                itemView.setOnClickListener {
                    itemClick(this)
                }
                itemView.setOnLongClickListener {
                    longItemClick(this)
                }
            }
        }

        private fun showDeadline(deadlineVal: Long) {
            taskDeadline.visibility = View.VISIBLE
            taskDeadlineIcon.visibility = View.VISIBLE
            taskDeadline.text = deadlineVal.toDateString()
        }
    }

}