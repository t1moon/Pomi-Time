package apps.tim.pomos.base.ui.stat

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.base.*
import apps.tim.pomos.base.data.Statistics
import apps.tim.pomos.base.ui.base.BaseFragment
import io.reactivex.functions.BiFunction
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.statistics_overall_item.*
import java.util.*
import javax.inject.Inject

class StatisticsFragment : BaseFragment() {
    private var total = 0
    private lateinit var currentStat: Statistics

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    lateinit var statisticsViewModel: StatisticsViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
        statisticsViewModel = ViewModelProviders.of(this, viewModelFactory)[StatisticsViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_statistics, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        observeToData()
        setButtonListeners()
        statisticsViewModel.loadStatistics()
        checkForInitialShowCase()
    }

    private fun observeToData() {
        statisticsViewModel.apply {
            statisticsItemsObservable
                    .zipWith(statisticsObservable, BiFunction(this@StatisticsFragment::setStatistics))
                    .subscribe()
                    .addTo(compositeDisposable)
            finishedObservable
                    .subscribe({ activity?.onBackPressed() },
                            this@StatisticsFragment::showError)
                    .addTo(compositeDisposable)
        }
    }

    private fun setButtonListeners() {
        newSessionBtn.setOnClickListener {
            statisticsViewModel.finishSession(currentStat)
        }
    }

    private fun setStatistics(items: List<StatisticsItem>, statistics: List<Statistics>) {
        calculateCurrentStat(items)
        val currentStats = statistics.toMutableList()
        currentStats.add(0, currentStat)

        taskList.layoutManager = LinearLayoutManager(context)
        val adapter = StatisticsAdapter(items, currentStats)
        adapter.totalDonePercentage = total
        taskList.adapter = adapter
        (taskList.adapter as StatisticsAdapter).notifyDataSetChanged()
    }


    //TODO shouldn't be here
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

    private fun checkForInitialShowCase() {
        if (ShowcaseHelper.isStatisticsShown())
            return
        ShowcaseHelper.showStatisticsShowcase(activity, newSessionBtn, this::getResultView)
    }

    // It is necessary to return resultView as function,
    // because resultView won't be null by the time it is requested
    private fun getResultView() : View = result
}