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
import kotlinx.android.synthetic.main.today_list_item.*


class TodayTasksAdapter(private val items: List<Task>, context: Context) : AddableAdapter(context) {

    override fun onCreateViewHolderDelegated(parent: ViewGroup): RecyclerView.ViewHolder {
        return TodayTaskHolder(LayoutInflater.from(context).inflate(R.layout.today_list_item, parent, false))

    }

    override fun onBindViewHolderDelegated(holder: RecyclerView.ViewHolder, position: Int) {
        holder as TodayTaskHolder
        holder.bind(items)
        holder.itemView?.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(TASK_ARG, items[getCorrectedPosition(position)])
            it.findNavController().navigate(R.id.action_tasksFragment_to_timerFragment, bundle)
        }

        holder.itemView?.setOnLongClickListener {
            val bundle = Bundle()
            bundle.putParcelable(TASK_ARG, items[getCorrectedPosition(position)])
            val picker = EditTaskFragment()
            picker.arguments = bundle
            picker.show((it.context as AppCompatActivity).fragmentManager, "Picker")
            true
        }
    }

    override fun getItemCount() = getCorrectedItemSize(items.size)

    class TodayTaskHolder(containerView: View)
        : DefaultHolder(containerView) {

        fun bind(items: List<Task>) {
            taskTitle.text = items[getCorrectedPosition()].title
            taskPomos.text = items[getCorrectedPosition()].pomodoros.toString()
            val deadlineVal = items[getCorrectedPosition()].deadline
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