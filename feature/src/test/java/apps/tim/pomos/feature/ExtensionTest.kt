package apps.tim.pomos.feature

import org.junit.Assert.assertTrue
import org.junit.Test
import java.util.*

class ExtensionTest {

    @Test
    fun calendarPrintableTest() {
        val cal1 = Calendar.getInstance()
        cal1.add(Calendar.DATE, -1)
        assertTrue(cal1.printDate() == "Yesterday")

        cal1.add(Calendar.DATE, -1)
        assertTrue(cal1.printDate() == cal1.time.toDateString())
    }
}