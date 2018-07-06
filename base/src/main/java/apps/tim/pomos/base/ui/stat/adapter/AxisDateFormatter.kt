package apps.tim.pomos.base.ui.stat.adapter

import apps.tim.pomos.base.data.entity.Statistics
import apps.tim.pomos.base.printDate
import com.github.mikephil.charting.components.AxisBase
import com.github.mikephil.charting.formatter.IAxisValueFormatter
import java.util.*

class AxisDateFormatter(private val stats: List<Statistics>) : IAxisValueFormatter {


    override fun getFormattedValue(value: Float, axis: AxisBase?): String {
        var index = value.toInt()

        /* If it is only one value, this code should be here */
        if (index < 0)
            index = 0
        if (index > stats.size - 1)
            index = stats.size - 1

        val valueCal = Calendar.getInstance()
        valueCal.timeInMillis = stats[index].date
        return valueCal.printDate()
    }

}