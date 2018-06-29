package apps.tim.pomos.feature.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.PreferenceHelper
import apps.tim.pomos.feature.PreferenceHelper.get
import apps.tim.pomos.feature.PreferenceHelper.set
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.PREF_DAILY_KEY
import apps.tim.pomos.feature.ui.PREF_REST_KEY
import apps.tim.pomos.feature.ui.PREF_WORK_KEY
import apps.tim.pomos.feature.ui.base.BaseFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val sp = PreferenceHelper.defaultPrefs(PomoApp.instance.applicationContext)
        val work = sp[PREF_WORK_KEY, 25]
        val rest = sp[PREF_REST_KEY, 5]
        val daily = sp[PREF_DAILY_KEY, 8]

        workNumber.setText(work.toString())
        restNumber.setText(rest.toString())
        dailyNumber.setText(daily.toString())

        backButton2.setOnClickListener {
            hideSoftKeyboard()
            activity?.findNavController(R.id.mainNavigationFragment)?.navigateUp()
        }
        save.setOnClickListener {
            val work = workNumber.text.toString().toInt()
            val rest = restNumber.text.toString().toInt()
            val daily = dailyNumber.text.toString().toInt()
            sp[PREF_WORK_KEY] = getValidatedWork(work)
            sp[PREF_REST_KEY] = getValidatedRest(rest)
            sp[PREF_DAILY_KEY] = getValidatedDaily(daily)
            hideSoftKeyboard()
            activity?.findNavController(R.id.mainNavigationFragment)?.navigateUp()
        }
    }

    fun getValidatedWork(work: Int) : Int {
        return when {
            work > 120 -> 120
            work <= 0 -> 1
            else -> work
        }
    }

    fun getValidatedRest(rest: Int) : Int {
        return when {
            rest > 60 -> 60
            rest <= 0 -> 1
            else -> rest
        }
    }

    fun getValidatedDaily(daily: Int) : Int {
        return when {
            daily > 24 -> 24
            daily <= 0 -> 1
            else -> daily
        }
    }
}