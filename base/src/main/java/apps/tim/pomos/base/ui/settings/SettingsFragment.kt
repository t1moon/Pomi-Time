package apps.tim.pomos.base.ui.settings

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.base.EMPTY_STRING
import apps.tim.pomos.base.R
import apps.tim.pomos.base.preference.SettingsPreference
import apps.tim.pomos.base.ui.base.BaseFragment
import apps.tim.pomos.base.ui.stat.StatisticsFragment
import kotlinx.android.synthetic.main.fragment_settings.*

class SettingsFragment : BaseFragment() {
    companion object {
        fun newInstance() = SettingsFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_settings, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val work = SettingsPreference.workDuration
        val rest = SettingsPreference.restDuration
        val daily = SettingsPreference.dailyGoal

        workNumber.setText(work.toString())
        restNumber.setText(rest.toString())
        dailyNumber.setText(daily.toString())

        backButton2.setOnClickListener {
            hideSoftKeyboard()
            activity?.onBackPressed()
        }
        save.setOnClickListener {
            val work = workNumber.text.toString().let {
                if (it.isEmpty()) 0 else it.toInt()
            }
            val rest = restNumber.text.toString().let {
                if (it.isEmpty()) 0 else it.toInt()
            }
            val daily = dailyNumber.text.toString().let {
                if (it.isEmpty()) 0 else it.toInt()
            }
            SettingsPreference.workDuration = getValidatedWork(work)
            SettingsPreference.restDuration = getValidatedRest(rest)
            SettingsPreference.dailyGoal = getValidatedDaily(daily)
            hideSoftKeyboard()
            activity?.onBackPressed()
        }
    }

    private fun getValidatedWork(work: Int) : Int {
        return when {
            work > 120 -> 120
            work <= 0 -> 1
            else -> work
        }
    }

    private fun getValidatedRest(rest: Int) : Int {
        return when {
            rest > 60 -> 60
            rest <= 0 -> 1
            else -> rest
        }
    }

    private fun getValidatedDaily(daily: Int) : Int {
        return when {
            daily > 24 -> 24
            daily <= 0 -> 1
            else -> daily
        }
    }
}