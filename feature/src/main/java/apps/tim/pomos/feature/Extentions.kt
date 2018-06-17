package apps.tim.pomos.feature

import android.content.res.Resources
import android.util.TypedValue


fun Resources.dipToPx(dip: Float) : Float {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dip, this.displayMetrics)
}
