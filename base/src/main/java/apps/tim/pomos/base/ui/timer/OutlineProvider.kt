package apps.tim.pomos.base.ui.timer

import android.annotation.TargetApi
import android.content.res.Resources
import android.graphics.Outline
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import apps.tim.pomos.base.R

internal class OutlineProvider(val resources: Resources) : ViewOutlineProvider() {

    private val rect: Rect = Rect()

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutline(view: View?, outline: Outline?) {
        view?.background?.copyBounds(rect)
        outline?.setRoundRect(rect, resources.getDimensionPixelSize(R.dimen.timer_radius_corner).toFloat())
    }
}
