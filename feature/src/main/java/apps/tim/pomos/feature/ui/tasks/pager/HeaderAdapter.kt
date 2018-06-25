package apps.tim.pomos.feature.ui.tasks.pager

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.R
import io.reactivex.Observable
import io.reactivex.subjects.PublishSubject
import kotlinx.android.extensions.LayoutContainer

const val DEFAULT_ITEM = 0
const val ADD_ITEM = 1

abstract class HeaderAdapter(val context: Context) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val addClickEvent = PublishSubject.create<Unit>()
    val getAddClickEvent: Observable<Unit> = addClickEvent
    fun getCorrectedPosition(pos: Int) = pos - 1
    fun getCorrectedItemSize(size: Int) = size + 1

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == DEFAULT_ITEM)
            onCreateViewHolderDelegated(parent)
        else
            HeaderViewHolder(LayoutInflater.from(context).inflate(R.layout.add_task_item, parent, false))
    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder.itemViewType == ADD_ITEM) {
            holder.itemView.setOnClickListener {
                addClickEvent.onNext(Unit)
            }
            return
        }
        onBindViewHolderDelegated(holder, position)
    }

    override fun getItemViewType(position: Int): Int {
        return if (position == 0)
            ADD_ITEM
        else
            DEFAULT_ITEM
    }

    class HeaderViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer

    open class DefaultHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {
        fun getCorrectedPosition() = adapterPosition - 1
    }

    abstract fun onCreateViewHolderDelegated(parent: ViewGroup): RecyclerView.ViewHolder
    abstract fun onBindViewHolderDelegated(holder: RecyclerView.ViewHolder, position: Int)
}