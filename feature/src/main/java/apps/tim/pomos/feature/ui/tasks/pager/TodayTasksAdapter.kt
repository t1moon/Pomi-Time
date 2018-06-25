package apps.tim.pomos.feature.ui.tasks.pager

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.toDateString
import apps.tim.pomos.feature.ui.DEFAULT_DATE_LONG
import apps.tim.pomos.feature.ui.TASK_ARG
import apps.tim.pomos.feature.ui.picker.EditTaskFragment
import apps.tim.pomos.feature.ui.tasks.data.Task
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.today_list_item.*


class TodayTasksAdapter(private val items: List<Task>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return TodayTaskHolder(LayoutInflater.from(context).inflate(R.layout.today_list_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as TodayTaskHolder
        holder.bind(items)
        holder.itemView.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(TASK_ARG, items[position])
            it.findNavController().navigate(R.id.action_tasksFragment_to_timerFragment, bundle)
        }

        holder.itemView.setOnLongClickListener {
            val bundle = Bundle()
            bundle.putParcelable(TASK_ARG, items[position])
            val picker = EditTaskFragment()
            picker.arguments = bundle
            picker.show((it.context as AppCompatActivity).fragmentManager, "Picker")
            true
        }
    }

    override fun getItemCount() = items.size

    class TodayTaskHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(items: List<Task>) {
            taskTitle.text = items[position].title
            taskPomos.text = items[position].pomodoros.toString()
            val deadlineVal = items[position].deadline
            if (deadlineVal != DEFAULT_DATE_LONG) {
                showDeadline(deadlineVal)
            }
        }

        private fun showDeadline(deadlineVal: Long) {
            taskDeadline.visibility = View.VISIBLE
            taskDeadlineIcon.visibility = View.VISIBLE
            taskDeadline.text = deadlineVal.toDateString()
        }
    }

}