package apps.tim.pomos.feature.ui.tasks.pager

import android.content.Context
import android.graphics.PorterDuff
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.tasks.data.Task



class TaskListAdapter(val items : List<Task>, val context: Context?): RecyclerView.Adapter<TaskListAdapter.TaskHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TaskHolder {
        return TaskHolder(LayoutInflater.from(context).inflate(R.layout.task_list_item, parent, false))
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: TaskHolder, position: Int) {
        holder.bind(items)
    }


    class TaskHolder(itemView: View?): RecyclerView.ViewHolder(itemView) {
        val taskTitle = itemView?.findViewById<TextView>(R.id.taskTitle)
        val pomosNumber = itemView?.findViewById<Button>(R.id.pomosNumber)

        fun bind(items: List<Task>) {
            taskTitle?.text = items[position].title
            pomosNumber?.background?.setColorFilter(
                    PomoApp.instance!!.color(R.color.colorPrimary),
                    PorterDuff.Mode.MULTIPLY)
        }
    }
}