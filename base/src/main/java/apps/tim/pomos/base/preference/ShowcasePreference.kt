package apps.tim.pomos.base.preference

import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.preference.PreferenceHelper.boolean

object ShowcasePreference {
    private val pref = PreferenceHelper.getPref(PomoApp.instance)

    var exampleTaskAdded by pref.boolean()
    var backlogPageShowcaseShown by pref.boolean()
    var tasksPageShowcaseShown by pref.boolean()
    var statisticsShowcaseShown by pref.boolean()
    var timerShowcaseShown by pref.boolean()
}

