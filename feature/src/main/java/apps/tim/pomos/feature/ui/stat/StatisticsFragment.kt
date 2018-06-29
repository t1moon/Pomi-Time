package apps.tim.pomos.feature.ui.stat

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.PreferenceHelper
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.base.BaseFragment
import apps.tim.pomos.feature.ui.tasks.TasksViewModel
import apps.tim.pomos.feature.ui.tasks.data.Statistics
import io.reactivex.Observable
import io.reactivex.functions.BiFunction
import kotlinx.android.synthetic.main.fragment_statistics.*
import java.util.*
import javax.inject.Inject

class StatisticsFragment : BaseFragment() {
    private var total = 0
    private lateinit var currentStat: Statistics

    @Inject
    lateinit var tasksViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        add(Observable.zip(
                tasksViewModel
                        .getStatisticsForToday().toObservable(),
                tasksViewModel.getStats().toObservable(), BiFunction { t1: List<StatisticsItem>, t2: List<Statistics> ->
            this.setStat(t1, t2)
        }).subscribe())

        newSessionBtn.setOnClickListener {
            context?.let {
                add(tasksViewModel.finishSession(currentStat).subscribe {
                    _ ->
                    activity?.onBackPressed()
                })
            }
        }
    }

    private fun setStat(items: List<StatisticsItem>, stats: List<Statistics>) {
        context?.let {
            for (i in stats)
                println(i)

            calculateCurrentStat(items)
            val currentStats = stats.toMutableList()
            currentStats.add(0, currentStat)

            taskList.layoutManager = LinearLayoutManager(context)
            val adapter = StatisticsAdapter(items, currentStats)
            adapter.totalDonePercentage = total
            taskList.adapter = adapter
            (taskList.adapter as StatisticsAdapter).notifyDataSetChanged()
        }
    }

    private fun calculateCurrentStat(items: List<StatisticsItem>) {
        val daily = PreferenceHelper.getDaily(PomoApp.instance)
        total = (items.fold(0)
        { total, next: StatisticsItem -> total + next.pomo } / daily.toFloat() * 100).toInt()
        currentStat = Statistics(
                id = 0,
                date = Calendar.getInstance().timeInMillis,
                completed = total
        )
    }


}