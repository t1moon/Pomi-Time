package apps.tim.pomos.base

import android.content.SharedPreferences
import apps.tim.pomos.base.PreferenceHelper.boolean

class ShowcasePreference(pref: SharedPreferences) {
    var exampleTaskAdded by pref.boolean()
    var backlogPageShowcaseShown by pref.boolean()
    var tasksPageShowcaseShown by pref.boolean()
    var statisticsShowcaseShown by pref.boolean()
    var timerShowcaseShown by pref.boolean()

}

