package apps.tim.pomos.feature.ui.tasks.pager

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.navigation.findNavController
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.TASK_ARG
import apps.tim.pomos.feature.ui.picker.EditTaskFragment
import apps.tim.pomos.feature.ui.tasks.data.Task


class TaskListAdapter(private val items : List<Task>, val context: Context): RecyclerView.Adapter<TaskListAdapter.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder(LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(items)
        holder.itemView?.setOnClickListener {
            val bundle = Bundle()
            bundle.putParcelable(TASK_ARG, items[position])
            it.findNavController().navigate(R.id.action_tasksFragment_to_timerFragment, bundle)}

        holder.itemView?.setOnLongClickListener {
            val bundle = Bundle()
            bundle.putParcelable(TASK_ARG, items[position])
            val picker = EditTaskFragment()
            picker.arguments = bundle
            picker.show((it.context as AppCompatActivity).fragmentManager, "Picker")
            true
        }
    }


    class TaskHolder(itemView: View?): RecyclerView.ViewHolder(itemView) {
        val taskTitle = itemView?.findViewById<TextView>(R.id.taskTitle)
        val pomosNumber = itemView?.findViewById<TextView>(R.id.pomosNumber)

        fun bind(items: List<Task>) {
            taskTitle?.text = items[adapterPosition].title
            pomosNumber?.text = items[adapterPosition].pomodoros.toString()
        }
    }
}