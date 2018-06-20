package apps.tim.pomos.feature.ui.tasks.pager

import android.content.Context
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.TASK_ARG
import apps.tim.pomos.feature.ui.picker.EditTaskFragment
import apps.tim.pomos.feature.ui.tasks.data.Task
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.synthetic.main.backlog_item.*
import kotlinx.android.synthetic.main.backlog_item.view.*


class BacklogAdapter(private val items: List<Task>, context: Context) : AddableAdapter(context) {
    private val activateTaskClick = PublishSubject.create<Task>()
    val activateTaskClickEvent: Observable<Task> = activateTaskClick

    override fun onCreateViewHolderDelegated(parent: ViewGroup): RecyclerView.ViewHolder {
        return BacklogTaskHolder(LayoutInflater.from(context).inflate(R.layout.backlog_item, parent, false))
    }

    override fun onBindViewHolderDelegated(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BacklogTaskHolder
        holder.bind(items)

        holder.itemView?.setOnLongClickListener {
            val bundle = Bundle()
            bundle.putParcelable(TASK_ARG, items[getCorrectedPosition(position)])
            val picker = EditTaskFragment()
            picker.arguments = bundle
            picker.show((it.context as AppCompatActivity).fragmentManager, "Picker")
            true
        }
        holder.itemView.transferButton.setOnClickListener {
            activateTaskClick.onNext(items[getCorrectedPosition(position)])
        }
    }

    override fun getItemCount() = getCorrectedItemSize(items.size)

    class BacklogTaskHolder(containerView: View)
        : DefaultHolder(containerView) {

        fun bind(items: List<Task>) {
            backlogTaskTitle.text = items[getCorrectedPosition()].title
            backlogPomosNumber.text = items[getCorrectedPosition()].pomodoros.toString()
        }
    }
}