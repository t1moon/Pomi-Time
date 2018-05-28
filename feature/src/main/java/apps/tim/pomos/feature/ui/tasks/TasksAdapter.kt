package apps.tim.pomos.feature.ui.tasks

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import apps.tim.pomos.feature.R

class TasksAdapter(val items : List<Task>, val context: Context?): RecyclerView.Adapter<TasksAdapter.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder(LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder?.taskTitle?.text = items.get(position).title
    }


    class TaskHolder(itemView: View?): RecyclerView.ViewHolder(itemView) {
        val taskTitle = itemView?.findViewById<TextView>(R.id.taskTitle)
    }
}