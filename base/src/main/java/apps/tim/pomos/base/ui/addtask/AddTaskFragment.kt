package apps.tim.pomos.base.ui.addtask

import android.app.DatePickerDialog
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.FragmentActivity
import android.view.View
import apps.tim.pomos.base.R
import apps.tim.pomos.base.app.PomoApp
import apps.tim.pomos.base.data.entity.Task
import apps.tim.pomos.base.di.ViewModelFactory
import apps.tim.pomos.base.toDateLong
import apps.tim.pomos.base.toDateString
import apps.tim.pomos.base.ui.base.BaseDialogFragment
import apps.tim.pomos.base.ui.tasks.TasksViewModel
import io.reactivex.rxkotlin.addTo
import kotlinx.android.synthetic.main.fragment_add.view.*
import java.util.*
import javax.inject.Inject


open class AddTaskFragment : BaseDialogFragment() {
    override fun getLayoutRes(): Int = R.layout.fragment_add

    @Inject
    lateinit var viewModelFactory: ViewModelFactory
    private lateinit var tasksViewModel: TasksViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        PomoApp.component.getFragmentComponent().inject(this)
        tasksViewModel = ViewModelProviders.of(activity as FragmentActivity, viewModelFactory)[TasksViewModel::class.java]
    }

    override fun initView(it: View) {
        setButtonListeners(it)
        setDatePicker(it)
    }
    private fun setButtonListeners(view: View) {
        view.run {
            this.ok.setOnClickListener {
                val task = Task(
                        id = 0,
                        title = this@run.taskTitle.text.toString(),
                        deadline = this@run.deadline.text.toString().toDateLong(),
                        created = Calendar.getInstance().timeInMillis,
                        isActive = this@run.active.isChecked
                )
                tasksViewModel.addTask(task)
                        .subscribe({}, this@AddTaskFragment::showError)
                        .addTo(compositeDisposable)
                dismiss()
            }
            this.cancel.setOnClickListener {
                dismiss()
            }
            this.removeDeadline.setOnClickListener {
                this@run.deadline.setText("")
                it.visibility = View.GONE
            }
        }
    }

    private fun setDatePicker(view: View) {
        view.let {
            val dateListener = DatePickerDialog.OnDateSetListener { _, year, monthOfYear, dayOfMonth ->
                val calendar = Calendar.getInstance()
                calendar.set(Calendar.YEAR, year)
                calendar.set(Calendar.MONTH, monthOfYear)
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth)
                it.deadline.setText(calendar.time.toDateString())
                it.removeDeadline.visibility = View.VISIBLE
            }
            val calendar = Calendar.getInstance()
            it.deadline.setOnClickListener {
                val datePickerDialog = DatePickerDialog(activity, dateListener, calendar
                        .get(Calendar.YEAR), calendar.get(Calendar.MONTH),
                        calendar.get(Calendar.DAY_OF_MONTH))
                datePickerDialog.datePicker.minDate = calendar.timeInMillis
                datePickerDialog.show()
            }
        }

    }



}