package apps.tim.pomos.base.ui.timer

import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v7.widget.GridLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.findNavController
import apps.tim.pomos.base.R
import apps.tim.pomos.base.TASK_ARG
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.data.entity.Task
import apps.tim.pomos.base.showcase.ShowcaseHelper
import apps.tim.pomos.base.ui.base.BaseFragment
import apps.tim.pomos.base.ui.timer.timerview.TimerView
import apps.tim.pomos.base.ui.timer.viewmodel.TimerViewModel
import apps.tim.pomos.base.ui.timer.viewmodel.TimerViewModelFactory
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_timer.*
import javax.inject.Inject

class TimerFragment : BaseFragment() {

    companion object {
        private val WORK_MODE = PomoApp.string(R.string.work)
        private val REST_MODE = PomoApp.string(R.string.rest)
        private const val TIMERVIEW_STATE = "TIMERVIEW_STATE"
        private const val TIMERCLOCK_STATE_KEY = "TIMERCLOCK_STATE"
        private const val CONTROL_DISABLED = "CONTROL_DISABLED"
    }

    @Inject
    lateinit var timerViewModelFactory: TimerViewModelFactory
    private lateinit var timerViewModel: TimerViewModel

    private lateinit var task: Task
    private lateinit var pomoAdapter: PomoAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        retainInstance = true
        PomoApp.component.getFragmentComponent().getTimerScreenComponent().inject(this)
        timerViewModel = ViewModelProviders.of(this, timerViewModelFactory)[TimerViewModel::class.java]
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return container?.inflate(R.layout.fragment_timer)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        if (savedInstanceState == null)
            resetTimer()
        else
            recoverTimer(savedInstanceState)

        setupHeaderTask()
        setupPomoList()
        observeToTimer()
        setUIListeners()
        showCase()
    }

    private fun resetTimer() {
        timerViewModel.refreshTimer()
        setTimerDefault(true)
    }

    private fun recoverTimer(savedInstanceState: Bundle) {
        timerView.setViewState(savedInstanceState.get(TIMERVIEW_STATE) as TimerView.TimerViewState)
        timerTime.text = savedInstanceState.get(TIMERCLOCK_STATE_KEY) as String
        if (savedInstanceState.get(CONTROL_DISABLED) as Boolean) {
            disableControls()
        }
    }

    private fun setupHeaderTask() {
        task = arguments?.get(TASK_ARG) as Task
        taskTitle.text = task.title
    }

    private fun setupPomoList() {
        pomoList.layoutManager = GridLayoutManager(context, 8)
        pomoAdapter = PomoAdapter()
        pomoList.adapter = pomoAdapter

        timerViewModel
                .getCompleted()
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe({
                    pomoAdapter.setPomo(it)
                    pomoAdapter.notifyDataSetChanged()
                }, this::showError)
                .addTo(compositeDisposable)
    }

    private fun observeToTimer() {
        timerViewModel.apply {
            getTimerState()
                .subscribe({
                    disableControls()
                    responseToTimeStateChanged(it)
                }, this@TimerFragment::showError)
                .addTo(compositeDisposable)
            getTimerProgress()
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe({ timerView.updateProgress(it) }, this@TimerFragment::showError)
                    .addTo(compositeDisposable)
            getTimerString()
                    .subscribe { timerTime.text = it }
                    .addTo(compositeDisposable)
        }
    }

    private fun setUIListeners() {
        backButton.setOnClickListener {
            activity?.findNavController(R.id.mainNavigationFragment)?.navigateUp()
        }
        settings.setOnClickListener {
            it.findNavController().navigate(R.id.action_timerFragment_to_settingsFragment)
        }
        timerView.setOnClickListener {
            timerViewModel.timerViewClicked()
        }
        timerView.setOnLongClickListener {
            timerViewModel.timerViewLongClicked()
            true
        }
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
        setTimerDefault(timerMode.text == REST_MODE)
    }

    private fun responseToTimeStateChanged(state: Timer.TimerState?) {
        when (state) {
            Timer.TimerState.STARTED -> timerView.start()
            Timer.TimerState.PAUSED -> timerView.pause()
            Timer.TimerState.PLAYED -> timerView.play()
            Timer.TimerState.CANCELLED -> {
                timerView.finish()
                setTimerDefault()
            }
            Timer.TimerState.FINISHED -> {
                timerView.finish()
                setTimerDefault(false)
                timerViewModel.addPomo(task.id)
                        .subscribe({
                            pomoAdapter.addPomo()
                        }, this::showError)
            }
        }
    }

    private fun setTimerDefault(isWork: Boolean = true) {
        timerViewModel.getDefaultTime(isWork)
                .subscribe({
                    timerTime.text = it
                }, this::showError)
                .addTo(compositeDisposable)
        timerMode.text = if (isWork) WORK_MODE else REST_MODE
        enableControls()
    }

    private fun enableControls() {
        leftControl.visibility = View.VISIBLE
        rightControl.visibility = View.VISIBLE
    }

    private fun disableControls() {
        leftControl.visibility = View.GONE
        rightControl.visibility = View.GONE
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(TIMERVIEW_STATE, timerView.getViewState())
        outState.putString(TIMERCLOCK_STATE_KEY, timerTime.text.toString())
        outState.putBoolean(CONTROL_DISABLED, leftControl.visibility == View.GONE)
    }

    private fun showCase() {
        ShowcaseHelper.showTimerShowcase(activity, pomoList)
    }

}