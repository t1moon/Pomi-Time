package apps.tim.pomos.base.ui.tasks.pager

import android.support.v7.util.DiffUtil
import apps.tim.pomos.base.ui.tasks.data.Task

class TaskDiffCallback : DiffUtil.ItemCallback<Task>() {
    override fun areContentsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem == newItem
    }

    override fun areItemsTheSame(oldItem: Task, newItem: Task): Boolean {
        return oldItem.id == newItem.id
    }
}