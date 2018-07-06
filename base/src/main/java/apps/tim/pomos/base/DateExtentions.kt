package apps.tim.pomos.base

import apps.tim.pomos.base.app.PomoApp
import java.text.SimpleDateFormat
import java.util.*

const val DATE_FORMAT = "d MMM"
const val TIME_FORMAT = "HH:mm"

fun Calendar.printDate(): String {
    val now = Calendar.getInstance()
    val isToday = now.get(Calendar.DATE) == this.get(Calendar.DATE)

    return when {
        isToday -> this.time.toTimeString()
        else -> this.time.toDateString()
    }
}

fun Date.toTimeString(timeFormat: String = TIME_FORMAT) : String {
    val sdf = SimpleDateFormat(timeFormat, Locale.US)
    return sdf.format(this)
}

fun Date.toDateString() : String {
    val locale = Locale(PomoApp.string(R.string.lang))
    val sdf = SimpleDateFormat(DATE_FORMAT, locale)
    return sdf.format(this)
}


fun Long.toDateString() : String {
    val locale = Locale(PomoApp.string(R.string.lang))
    val sdf = SimpleDateFormat(DATE_FORMAT, locale)
    return sdf.format(Date(this))
}

fun String.toDateLong() : Long {
    if (this.isEmpty())
        return DEFAULT_DATE_LONG
    val locale = Locale(PomoApp.string(R.string.lang))
    val sdf = SimpleDateFormat(DATE_FORMAT, locale)
    return sdf.parse(this).time
}
