package apps.tim.pomos.base.ui.timer

import android.app.Activity
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import apps.tim.pomos.base.*
import apps.tim.pomos.base.ui.TASK_ARG
import apps.tim.pomos.base.ui.base.BaseFragment
import apps.tim.pomos.base.ui.tasks.data.Task
import io.reactivex.android.schedulers.AndroidSchedulers
import kotlinx.android.synthetic.main.fragment_timer.*
import javax.inject.Inject

class TimerFragment : BaseFragment() {

    companion object {
        private val WORK_MODE = PomoApp.string(R.string.work)
        private val REST_MODE = PomoApp.string(R.string.rest)
    }

    @Inject
    lateinit var timerViewModel: TimerViewModel

    private lateinit var task: Task
    private lateinit var pomoAdapter: PomoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().getTimerScreenComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        timerViewModel.refreshTimer()
        return inflater.inflate(R.layout.fragment_timer, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        setupHeader()
        setupPomoList()
        setupTimer()
        settings.setOnClickListener {
            it.findNavController().navigate(R.id.action_timerFragment_to_settingsFragment)
        }
        checkForShowcase()
    }

    private fun checkForShowcase() {
        val showcasePreference = ShowcasePreference(PreferenceHelper.defaultPrefs(PomoApp.instance))
        if (showcasePreference.timerShowcaseShown)
            return
        ShowCase.getTargetView(activity as Activity, pomoList, ShowCase.Type.DAILY, 150)
        showcasePreference.timerShowcaseShown = true
    }

    private fun setupPomoList() {
        add(timerViewModel.getCompleted()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe {
                    pomoList.layoutManager = GridLayoutManager(context, 8)
                    pomoAdapter = PomoAdapter(it)
                    pomoList.adapter = pomoAdapter
                })
    }

    private fun setupHeader() {
        task = arguments?.get(TASK_ARG) as Task
        taskTitle.text = task.title
        backButton.setOnClickListener {
            activity?.findNavController(R.id.mainNavigationFragment)?.navigateUp()
        }
    }

    private fun setupTimer() {
        resetTimer(true)
        setTimerView()
        setControlButtons()
    }

    private fun setControlButtons() {
        leftControl.setOnClickListener {
            timerViewModel.timerViewModeChanged()
            changeTimerMode()
        }
        rightControl.setOnClickListener {
            timerViewModel.timerViewModeChanged()
            changeTimerMode()
        }
    }

    private fun changeTimerMode() {
        resetTimer(timerMode.text == REST_MODE)
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
                    disableControls()
                    when (it) {
                        Timer.TimerState.STARTED -> timerView.start()
                        Timer.TimerState.PAUSED -> timerView.pause()
                        Timer.TimerState.PLAYED -> timerView.play()
                        Timer.TimerState.CANCELLED -> {
                            timerView.finish()
                            resetTimer()
                        }
                        Timer.TimerState.FINISHED -> {
                            timerView.finish()
                            resetTimer(false)
                            timerViewModel.addPomo(task.id)
                                    .subscribe { _, _ ->
                                        pomoAdapter.addPomo()
                                    }
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

    private fun resetTimer(isWork: Boolean = true) {
        add(timerViewModel.defaultTime(isWork)
                .subscribe {
                    timerTime.text = it
                })
        add(timerViewModel.getTimerString().subscribe {
            timerTime.text = it
        })
        timerMode.text = if (isWork) WORK_MODE else REST_MODE

        enableControls()
    }

    private fun disableControls() {
        leftControl.visibility = View.GONE
        rightControl.visibility = View.GONE
    }

    private fun enableControls() {
        leftControl.visibility = View.VISIBLE
        rightControl.visibility = View.VISIBLE
    }

    override fun onDestroyView() {
        super.onDestroyView()
        timerViewModel.stopTimer()
    }

}