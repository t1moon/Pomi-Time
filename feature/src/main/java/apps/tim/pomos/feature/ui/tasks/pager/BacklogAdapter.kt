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
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.backlog_item.*
import kotlinx.android.synthetic.main.backlog_item.view.*


class BacklogAdapter(private val items: List<Task>, val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return BacklogTaskHolder(LayoutInflater.from(context).inflate(R.layout.backlog_item, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        holder as BacklogTaskHolder
        holder.bind(items)

        holder.itemView.setOnLongClickListener {
            val bundle = Bundle()
            bundle.putParcelable(TASK_ARG, items[position])
            val picker = EditTaskFragment()
            picker.arguments = bundle
            picker.show((it.context as AppCompatActivity).fragmentManager, "Picker")
            true
        }
        holder.itemView.transferButton.setOnClickListener {
            activateTaskClick.onNext(items[position])
        }
    }

    private val activateTaskClick = PublishSubject.create<Task>()
    val activateTaskClickEvent: Observable<Task> = activateTaskClick

    override fun getItemCount() = items.size

    class BacklogTaskHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(items: List<Task>) {
            backlogTaskTitle.text = items[position].title
        }
    }
}