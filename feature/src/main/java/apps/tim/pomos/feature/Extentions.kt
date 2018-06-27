package apps.tim.pomos.feature

import android.content.res.Resources
import android.util.TypedValue
import apps.tim.pomos.feature.ui.DATE_FORMAT
import apps.tim.pomos.feature.ui.DEFAULT_DATE_LONG
import apps.tim.pomos.feature.ui.TIME_FORMAT
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
    val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
    return sdf.parse(this).time
}

fun Date.toDateString(dateFormat: String = DATE_FORMAT) : String {
    val sdf = SimpleDateFormat(dateFormat, Locale.US)
    return sdf.format(this)
}

fun Date.toTimeString(timeFormat: String = TIME_FORMAT) : String {
    val sdf = SimpleDateFormat(timeFormat, Locale.US)
    return sdf.format(this)
}

fun Calendar.printDate(yesterday: String = "Yesterday", dateFormat: String = DATE_FORMAT): String {
    val now = Calendar.getInstance()
    val isToday = now.get(Calendar.DATE) == this.get(Calendar.DATE)
    val isYesterday = now.get(Calendar.DATE) - this.get(Calendar.DATE) == 1
    return when {
        isToday -> this.time.toTimeString()
        isYesterday-> yesterday
        else -> this.time.toDateString(dateFormat)
    }
}


fun Long.toDateString() : String {
    val sdf = SimpleDateFormat(DATE_FORMAT, Locale.US)
    return sdf.format(Date(this))
}
