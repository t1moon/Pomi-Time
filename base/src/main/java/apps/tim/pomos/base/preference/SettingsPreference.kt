package apps.tim.pomos.base.preference

import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.preference.PreferenceHelper.int

object SettingsPreference {
    private val pref = PreferenceHelper.getPref(PomoApp.instance)
    private const val PREF_WORK_KEY = "WORK"
    private const val PREF_REST_KEY = "REST"
    private const val PREF_DAILY_KEY = "DAILY"

    private const val WORK_DURATION_IN_MINUTE = 25
    private const val REST_DURATION_IN_MINUTE = 5
    private const val DAILY_GOAL = 8

    var workDuration by pref.int(def = WORK_DURATION_IN_MINUTE, key = PREF_WORK_KEY)
    var restDuration by pref.int(def = REST_DURATION_IN_MINUTE, key = PREF_REST_KEY)
    var dailyGoal by pref.int(def = DAILY_GOAL, key = PREF_DAILY_KEY)

}