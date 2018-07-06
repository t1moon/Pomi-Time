package apps.tim.pomos.base.ui.timer

import android.animation.ValueAnimator
import android.graphics.PorterDuff
import android.graphics.PorterDuffColorFilter
import android.os.Build
import android.os.Handler
import android.support.annotation.RequiresApi
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.widget.ImageView
import apps.tim.pomos.base.R
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.preference.SettingsPreference
import kotlinx.android.extensions.LayoutContainer
import kotlinx.android.synthetic.main.item_pomo.*


class PomoAdapter(var pomos: Int = 0) :
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
            Handler().post {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    flickerPomo(holder.pomo)
                }
            }
    }

    override fun getItemCount() = SettingsPreference.dailyGoal


    class ViewHolder(override val containerView: View)
        : RecyclerView.ViewHolder(containerView), LayoutContainer {

        fun bind(pomos: Int, pos: Int) {
            when {
                pos > pomos -> pomo.setImageResource(R.drawable.ic_timer_blue)
                else -> pomo.setImageResource(R.drawable.ic_timer)
            }
        }

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun flickerPomo(pomo: ImageView) {
        val red = PomoApp.color(R.color.colorAccent)
        val blue = PomoApp.color(R.color.iconColor)
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

    fun setPomo(it: Int) {
        pomos = it
    }

}
