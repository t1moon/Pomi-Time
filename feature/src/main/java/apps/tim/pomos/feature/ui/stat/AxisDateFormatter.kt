package apps.tim.pomos.feature.ui.stat

import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.printDate
import apps.tim.pomos.feature.ui.tasks.data.Statistics
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.util.*

class AxisDateFormatter(private val stats: List<Statistics>) : IAxisValueFormatter {


    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        val valueCal = Calendar.getInstance()
        valueCal.timeInMillis = stats[value.toInt() - 1].date
        return valueCal.printDate(PomoApp.string(R.string.yesterday))
    }

}