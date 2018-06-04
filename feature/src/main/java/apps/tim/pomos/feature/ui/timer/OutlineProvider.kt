package apps.tim.pomos.feature.ui.timer

import android.annotation.TargetApi
import android.content.res.Resources
import android.graphics.Outline
import android.graphics.Rect
import android.os.Build
import android.view.View
import android.view.ViewOutlineProvider
import apps.tim.pomos.feature.R

internal class OutlineProvider(val resources: Resources, val padding: Int) : ViewOutlineProvider() {

    private val rect: Rect = Rect()

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    override fun getOutline(view: View?, outline: Outline?) {
        view?.background?.copyBounds(rect)
//        rect.padding(padding)
        outline?.setRoundRect(rect, resources.getDimensionPixelSize(R.dimen.timer_radius_corner).toFloat())
    }
}

private fun Rect.padding(padding: Int) {
    val newWidth = width() - padding * 2
    val newHeight = height() - padding * 2
    val deltaX = (width() - newWidth) / 2
    val deltaY = (height() - newHeight) / 2

    set((left + deltaX), (top + deltaY), (right - deltaX), (bottom - deltaY))
}
