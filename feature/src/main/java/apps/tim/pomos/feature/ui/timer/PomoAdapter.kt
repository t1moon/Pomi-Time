package apps.tim.pomos.feature.ui.timer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.R
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pomo.*


class PomoAdapter(var pomos: Int) :
        RecyclerView.Adapter<PomoAdapter.ViewHolder>() {

    fun addPomo() {
        pomos++
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pomo, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pomos, position + 1)
    }

    override fun getItemCount() = 8


    class ViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(pomos: Int, pos: Int) {
           if (pos > pomos)
               pomo.setImageResource(R.drawable.ic_timer_blue)
        }
    }
}
