package apps.tim.pomos.base

import android.app.Activity
import android.support.v4.app.FragmentActivity
import android.support.v4.content.res.ResourcesCompat
import android.view.View
import apps.tim.pomos.base.ShowcaseHelper.Type.*
import apps.tim.pomos.base.data.Task
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetSequence
import com.getkeepsafe.taptargetview.TapTargetView
import java.util.*


object ShowcaseHelper {
    enum class Type { TODAY, BACKLOG, FINISH, POMOS, TRANSFER, DONE, NEWSESSION, DAILY }

    fun getTarget(view: View, type: Type, radius: Int = 70): TapTarget {
        val (title, description) = getTextForPos(type)
        return TapTarget.forView(view, title, description)
                .outerCircleColor(R.color.colorAccent)
                .outerCircleAlpha(0.85f)
                .textTypeface(ResourcesCompat.getFont(view.context, R.font.googlesans_regular))
                .titleTextSize(24)
                .descriptionTextSize(16)
                .textColor(R.color.colorPrimary)
                .descriptionTextAlpha(0.75f)
                .drawShadow(true)
                .cancelable(true)
                .transparentTarget(true)
                .targetRadius(radius)
    }

    fun getTargetView(activity: Activity, view: View, type: Type, radius: Int = 70): TapTargetView {
        return TapTargetView.showFor(activity, getTarget(view, type, radius))
    }

    private fun getTextForPos(type: Type): Text {
        return when (type) {
            TODAY -> Text(PomoApp.string(R.string.today_tasks_title), PomoApp.string(R.string.today_tasks_description))
            BACKLOG -> Text(PomoApp.string(R.string.backlog_tasks_title), PomoApp.string(R.string.backlog_tasks_description))
            FINISH -> Text(PomoApp.string(R.string.finish_session_title), PomoApp.string(R.string.finish_session_description))
            POMOS -> Text(PomoApp.string(R.string.task_title), PomoApp.string(R.string.task_description))
            TRANSFER -> Text(PomoApp.string(R.string.task_transfer_title), PomoApp.string(R.string.task_transfer_description))
            DONE -> Text(PomoApp.string(R.string.done_title), PomoApp.string(R.string.done_description))
            NEWSESSION -> Text(PomoApp.string(R.string.new_session_title), PomoApp.string(R.string.new_session_description))
            DAILY -> Text(PomoApp.string(R.string.daily_title), PomoApp.string(R.string.daily_description))
        }
    }

    fun getTodayExample(): Task {
        return Task(
                id = 0,
                title = PomoApp.string(R.string.today_exampletask_title),
                deadline = Calendar.getInstance().timeInMillis,
                pomo = 3,
                currentPomo = 3,
                created = Calendar.getInstance().timeInMillis,
                isActive = true
        )
    }

    fun getBacklogExample(): Task {
        return Task(
                id = 0,
                title = PomoApp.string(R.string.backlog_exampletask_title),
                pomo = 0,
                created = Calendar.getInstance().timeInMillis,
                isActive = false
        )
    }

    fun isShowcaseItemsAdded(): Boolean {
        val showcasePreference = ShowcasePreference(PreferenceHelper.defaultPrefs(PomoApp.instance))
        return showcasePreference.exampleTaskAdded
    }

    fun setShowcaseItemsAdded() {
        val showcasePreference = ShowcasePreference(PreferenceHelper.defaultPrefs(PomoApp.instance))
        showcasePreference.exampleTaskAdded = true
    }

    fun showStatisticsShowcase(activity: FragmentActivity?, buttonNewSession: View, getResultView: () -> View) {
        val showcasePreference = ShowcasePreference(PreferenceHelper.defaultPrefs(PomoApp.instance))
        if (showcasePreference.statisticsShowcaseShown)
            return

        TapTargetSequence(activity)
                .targets(ShowcaseHelper.getTarget(buttonNewSession, ShowcaseHelper.Type.NEWSESSION))
                .continueOnCancel(true)
                .listener(object : TapTargetSequence.Listener {
                    override fun onSequenceCanceled(lastTarget: TapTarget?) {}
                    override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}

                    override fun onSequenceFinish() {
                        ShowcaseHelper.getTargetView(activity as Activity, getResultView.invoke(), ShowcaseHelper.Type.DONE)
                        showcasePreference.statisticsShowcaseShown = true
                    }
                })
                .start()
    }

    fun showTasksFragmentShowcase(activity: FragmentActivity?, getPomoView: () -> View, vararg views: View) {
        val showcasePreference = ShowcasePreference(PreferenceHelper.defaultPrefs(PomoApp.instance))
        if (showcasePreference.tasksPageShowcaseShown)
            return

        TapTargetSequence(activity)
                .targets(
                        ShowcaseHelper.getTarget(views[0], ShowcaseHelper.Type.TODAY),
                        ShowcaseHelper.getTarget(views[1], ShowcaseHelper.Type.BACKLOG),
                        ShowcaseHelper.getTarget(views[2], ShowcaseHelper.Type.FINISH))
                .continueOnCancel(true)
                .listener(object : TapTargetSequence.Listener {
                    override fun onSequenceCanceled(lastTarget: TapTarget?) {}
                    override fun onSequenceStep(lastTarget: TapTarget?, targetClicked: Boolean) {}

                    override fun onSequenceFinish() {
                        ShowcaseHelper.getTargetView(activity as Activity, getPomoView.invoke(), ShowcaseHelper.Type.POMOS)
                        showcasePreference.tasksPageShowcaseShown = true
                    }
                })
                .start()
    }

    fun showBacklogPageShowcase(activity: FragmentActivity?, transferBtn: View) {
        val showcasePreference = ShowcasePreference(PreferenceHelper.defaultPrefs(PomoApp.instance))
        if (showcasePreference.backlogPageShowcaseShown)
            return

        ShowcaseHelper.getTargetView(activity as Activity, transferBtn, ShowcaseHelper.Type.TRANSFER)
        showcasePreference.backlogPageShowcaseShown = true
    }

    fun showTimerShowcase(activity: FragmentActivity?, vararg views: View) {
        val showcasePreference = ShowcasePreference(PreferenceHelper.defaultPrefs(PomoApp.instance))
        if (showcasePreference.timerShowcaseShown)
            return

        ShowcaseHelper.getTargetView(activity as Activity, views[0], ShowcaseHelper.Type.DAILY, 150)
        showcasePreference.timerShowcaseShown = true
    }

    data class Text(val title: String, val description: String)

}
