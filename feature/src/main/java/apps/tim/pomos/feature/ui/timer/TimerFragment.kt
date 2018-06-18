package apps.tim.pomos.feature.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import apps.tim.pomos.feature.PomoApp
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.TASK_ARG
import apps.tim.pomos.feature.ui.base.BaseFragment
import apps.tim.pomos.feature.ui.tasks.data.Task
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_timer.*
import javax.inject.Inject

class TimerFragment : BaseFragment() {

    @Inject
    lateinit var timerViewModel: TimerViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupHeader()
        setupTimer()
    }

    private fun setupHeader() {
        val task = arguments?.get(TASK_ARG) as Task
        taskTitle.text = task.title
        backButton.setOnClickListener { activity?.findNavController(R.id.mainNavigationFragment)?.navigateUp() }
    }

    private fun setupTimer() {
        setTimerString()
        setTimerView()
    }

    private fun setTimerView() {
        timerView.setOnClickListener {
            timerViewModel.timerViewClicked()
        }
        timerView.setOnLongClickListener {
            timerViewModel.timerViewLongClicked()
            true
        }

        add(timerViewModel.getTimerState()
                .subscribe {
                    when (it) {
                        Timer.TimerState.STARTED -> timerView.start()
                        Timer.TimerState.PAUSED -> timerView.pause()
                        Timer.TimerState.PLAYED -> timerView.play()
                        Timer.TimerState.FINISHED -> {
                            timerView.finish()
                            setTimerString()
                        }
                    }
                }
        )
        add(timerViewModel.getTimerProgress()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    timerView.updateProgress(it)
                })
    }

    private fun setTimerString() {
        add(timerViewModel.defaultTime()
                .subscribe {
                    timerTime.text = it
                })
        add(timerViewModel.getTimerString().subscribe {
            timerTime.text = it
        })

    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerViewModel.stopTimer()
    }

}