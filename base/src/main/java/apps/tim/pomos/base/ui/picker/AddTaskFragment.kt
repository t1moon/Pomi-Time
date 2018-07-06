package apps.tim.pomos.base.ui.picker

import android.app.DatePickerDialog
import android.app.Dialog
import android.app.DialogFragment
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import apps.tim.pomos.base.PomoApp
import apps.tim.pomos.base.R
import apps.tim.pomos.base.toDateLong
import apps.tim.pomos.base.toDateString
import apps.tim.pomos.base.ui.tasks.TasksViewModel
import apps.tim.pomos.base.data.Task
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import kotlinx.android.synthetic.main.fragment_add.*
import java.util.*
import javax.inject.Inject


open class AddTaskFragment : DialogFragment() {
    private val compositeDisposable = CompositeDisposable()

    @Inject
    lateinit var tasksViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_add, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        taskTitle.requestFocus()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE)

        setOkButtonClicked()
        cancel.setOnClickListener { dismiss() }
        setDatePicker()
        removeDeadline.setOnClickListener {
            deadline.setText("")
            it.visibility = View.GONE
        }
    }

    protected open fun setOkButtonClicked() {
        ok.setOnClickListener {
            val task = Task(
                    id = 0,
                    title = taskTitle.text.toString(),
                    deadline = deadline.text.toString().toDateLong(),
                    created = Calendar.getInstance().timeInMillis,
                    isActive = active.isChecked
            )
            add(tasksViewModel.addTask(task).subscribe())
            dismiss()
        }
    }

    private fun setDatePicker() {
        val dateListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
            val calendar = Calendar.getInstance()
            calendar.set(Calendar.YEAR, year)
            calendar.set(Calendar.MONTH, monthOfYear)
            calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
            deadline.setText(calendar.time.toDateString())
            removeDeadline.visibility = View.VISIBLE
        }
        val calendar = Calendar.getInstance()
        deadline.setOnClickListener {
            val datePickerDialog = DatePickerDialog(activity, dateListener, calendar
                    .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                    calendar.get(Calendar.DAY_OF_MONTH))
            datePickerDialog.datePicker.minDate = calendar.timeInMillis
            datePickerDialog.show()

        }
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val d = super.onCreateDialog(savedInstanceState)
        d.window.setBackgroundDrawableResource(R.drawable.picker_layout_background)
        return d
    }


    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    protected fun add(d: Disposable) {
        compositeDisposable.add(d)
    }
}