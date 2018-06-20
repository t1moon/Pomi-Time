package apps.tim.pomos.feature.ui.timer

import android.animation.ValueAnimator
import android.annotation.TargetApi
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Handler
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import apps.tim.pomos.feature.PomoApp
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
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_pomo, parent, false)
        return ViewHolder(view)
    }


    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(pomos, position + 1)

        if (position == pomos)
            Handler().post({
                flickerPomo(holder.pomo)
            })
    }

    override fun getItemCount() = 8


    class ViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(pomos: Int, pos: Int) {
            when {
                pos > pomos -> pomo.setImageResource(R.drawable.ic_timer_blue)
                else -> pomo.setImageResource(R.drawable.ic_timer)
            }
        }

    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private fun flickerPomo(pomo: ImageView) {
        val red = PomoApp.color(R.color.timerIcon)
        val blue = PomoApp.color(R.color.timerIconFaded)
        ValueAnimator.ofArgb(blue, red).apply {
            duration = 1000
            addUpdateListener {
                val porterDuffColorFilter = PorterDuffColorFilter(it.animatedValue as Int,
                        PorterDuff.Mode.SRC_ATOP)
                pomo.colorFilter = porterDuffColorFilter
            }
            repeatCount = Animation.INFINITE
            start()
        }
    }

}
