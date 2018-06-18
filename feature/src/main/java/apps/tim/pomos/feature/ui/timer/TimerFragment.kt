package apps.tim.pomos.feature.ui.timer

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import apps.tim.pomos.feature.R
import apps.tim.pomos.feature.ui.TASK_ARG
import apps.tim.pomos.feature.ui.TASK_DURATION
import apps.tim.pomos.feature.ui.base.BaseFragment
import apps.tim.pomos.feature.ui.tasks.data.Task
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_timer.*

class TimerFragment : BaseFragment() {
    enum class TimerState { PLAYING, PAUSED, STOPED }

    private val timer = Timer()
    private var timerState: TimerState = TimerState.STOPED
    private lateinit var timerDisposable: Disposable


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
        timerTime.text = "$TASK_DURATION:00"
        timerView.setOnClickListener {
            timerState = when (timerState) {
                TimerState.STOPED -> {
                    play(true)
                    TimerState.PLAYING
                }
                TimerState.PAUSED -> {
                    play(false)
                    TimerState.PLAYING
                }
                TimerState.PLAYING -> {
                    pause()
                    TimerState.PAUSED
                }
            }
            timerView.state = timerState
        }
        timerView.setOnLongClickListener { timerView.cancel(); true }
    }

    private fun pause() {
        timerView.pause()
        compositeDisposable.remove(timerDisposable)
    }

    private fun play(fromStop: Boolean) {
        if (fromStop)
            timerView.startAnimation()

        timerView.play()
        timerDisposable = timer.start().subscribe { timerTime.text = it }
        compositeDisposable.add(timerDisposable)
    }

}