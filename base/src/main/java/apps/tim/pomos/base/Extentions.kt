package apps.tim.pomos.base

import android.app.Activity
import android.content.Context
import android.content.res.Resources
import android.util.TypedValue
import android.widget.Toast
import apps.tim.pomos.base.ui.DATE_FORMAT
import apps.tim.pomos.base.ui.DEFAULT_DATE_LONG
import apps.tim.pomos.base.ui.TIME_FORMAT
import java.text.SimpleDateFormat
import java.util.*


fun Resources.dipToPx(dip: Float) : Float {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dip, this.displayMetrics)
}

fun String.toInt() : Int {
    if (this.isEmpty())
        return 0
    return Integer.parseInt(this)
}

fun String.toDateLong() : Long {
    if (this.isEmpty())
        return DEFAULT_DATE_LONG
    val locale = Locale(PomoApp.string(R.string.lang))
    val sdf = SimpleDateFormat(DATE_FORMAT, locale)
    return sdf.parse(this).time
}

fun Date.toDateString(dateFormat: String = DATE_FORMAT) : String {
    val locale = Locale(PomoApp.string(R.string.lang))
    val sdf = SimpleDateFormat(dateFormat, locale)
    return sdf.format(this)
}

fun Date.toTimeString(timeFormat: String = TIME_FORMAT) : String {
    val sdf = SimpleDateFormat(timeFormat, Locale.US)
    return sdf.format(this)
}

fun Calendar.printDate(dateFormat: String = DATE_FORMAT): String {
    val now = Calendar.getInstance()
    val isToday = now.get(Calendar.DATE) == this.get(Calendar.DATE)

    return when {
        isToday -> this.time.toTimeString()
        else -> this.time.toDateString(dateFormat)
    }
}


fun Long.toDateString() : String {
    val locale = Locale(PomoApp.string(R.string.lang))
    val sdf = SimpleDateFormat(DATE_FORMAT, locale)
    return sdf.format(Date(this))
}

fun Activity.showError() {
    Toast.makeText(this, PomoApp.string(R.string.error_text), Toast.LENGTH_SHORT).show()
}