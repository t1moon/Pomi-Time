package apps.tim.pomos.base.ui.stat

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.base.R
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.di.ViewModelFactory
import apps.tim.pomos.base.showcase.ShowcaseHelper
import apps.tim.pomos.base.ui.base.BaseFragment
import apps.tim.pomos.base.ui.stat.adapter.StatisticsAdapter
import apps.tim.pomos.base.ui.stat.viewmodel.StatisticsViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_statistics.*
import kotlinx.android.synthetic.main.statistics_overall_item.*
import javax.inject.Inject

class StatisticsFragment : BaseFragment() {
    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var statisticsViewModel: StatisticsViewModel

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
        statisticsViewModel.loadStatistics()
        showCase()
        observeToData()
    }

    private fun observeToData() {
        statisticsViewModel.apply {
            statsReadyObservable
                    .subscribe(this@StatisticsFragment::setStatistics, this@StatisticsFragment::showError)
                    .addTo(compositeDisposable)
            finishedObservable
                    .subscribe({ activity?.onBackPressed() },
                            this@StatisticsFragment::showError)
                    .addTo(compositeDisposable)
        }
    }


    private fun setStatistics(combined: StatisticsViewModel.CombinedStatistics) {
        val items = combined.items
        val statistics = combined.stats

        taskList.layoutManager = LinearLayoutManager(context)
        val adapter = StatisticsAdapter(items, statistics)
        adapter.totalDonePercentage = statistics.first().completed
        taskList.adapter = adapter
        (taskList.adapter as StatisticsAdapter).notifyDataSetChanged()
        newSessionBtn.setOnClickListener {
            statisticsViewModel.finishSession(statistics.first())
        }
    }

    private fun showCase() {
        ShowcaseHelper.showStatisticsShowcase(activity, newSessionBtn, this::getResultView)
    }

    // It is necessary to return resultView as function,
    // because resultView won't be null by the time it is requested
    private fun getResultView() : View = result
}