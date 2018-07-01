package apps.tim.pomos.base

import android.app.Activity
import android.view.View
import apps.tim.pomos.base.ShowCase.Type.*
import com.getkeepsafe.taptargetview.TapTarget
import com.getkeepsafe.taptargetview.TapTargetView


object ShowCase {
    enum class Type { TODAY, BACKLOG, FINISH, POMOS, TRANSFER, DONE, NEWSESSION, DAILY }

    fun getTarget(view: View, type: Type, radius: Int = 70): TapTarget {
        val (title, description) = getTextForPos(type)
        return TapTarget.forView(view, title, description)
                .outerCircleColor(R.color.colorAccent)
                .outerCircleAlpha(0.80f)
                .titleTextSize(40)
                .descriptionTextSize(20)
                .textColor(R.color.colorPrimary)
                .descriptionTextAlpha(0.8f)
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
            NEWSESSION ->Text(PomoApp.string(R.string.new_session_title), PomoApp.string(R.string.new_session_description))
            DAILY ->Text(PomoApp.string(R.string.daily_title), PomoApp.string(R.string.daily_description))
        }
    }

    data class Text(val title: String, val description: String)

}
