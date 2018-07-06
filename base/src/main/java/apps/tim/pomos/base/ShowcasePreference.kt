package apps.tim.pomos.base

import android.content.SharedPreferences
import apps.tim.pomos.base.PreferenceHelper.boolean

class ShowcasePreference(pref: SharedPreferences) {
    var exampleTask by pref.boolean()
    var backlogShowcaseShown by pref.boolean()
    var todayShowcaseShown by pref.boolean()
    var statisticsShowcaseShown by pref.boolean()
    var timerShowcaseShown by pref.boolean()

}

