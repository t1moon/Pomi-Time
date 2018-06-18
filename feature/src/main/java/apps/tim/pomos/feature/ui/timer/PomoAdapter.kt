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
        pomos += 1
        notifyDataSetChanged()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_pomo, parent, false))
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
            else
               pomo.setImageResource(R.drawable.ic_timer)
        }
    }
}
