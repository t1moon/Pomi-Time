package apps.tim.pomos.base.ui.tasks.pager.backlog

import android.support.v7.recyclerview.extensions.ListAdapter
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.base.R
import apps.tim.pomos.base.toDateString
import apps.tim.pomos.base.ui.DEFAULT_DATE_LONG
import apps.tim.pomos.base.ui.tasks.data.Task
import apps.tim.pomos.base.ui.tasks.pager.TaskDiffCallback
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.backlog_list_item.*


class BacklogAdapter(private val transferButtonClick: (Task) -> Unit,
                     private val longItemClick: (Task) -> Boolean)
    : ListAdapter<Task, RecyclerView.ViewHolder>(TaskDiffCallback()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BacklogTaskHolder(LayoutInflater.from(parent.context).inflate(R.layout.backlog_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BacklogTaskHolder
        holder.bind(getItem(position), transferButtonClick, longItemClick)
    }

    class BacklogTaskHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(task: Task,
                 transferButtonClick: (Task) -> Unit,
                 longItemClick: (Task) -> Boolean) {
            with(task) {
                taskTitle.text = task.title
                taskPomos.text = task.pomo.toString()
                val deadlineVal = task.deadline
                if (deadlineVal != DEFAULT_DATE_LONG) {
                    showDeadline(deadlineVal)
                }
                transferButton.setOnClickListener {
                    transferButtonClick(this)
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