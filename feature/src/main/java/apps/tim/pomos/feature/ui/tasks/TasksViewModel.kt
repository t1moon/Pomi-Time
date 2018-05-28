package apps.tim.pomos.feature.ui.tasks

import android.arch.lifecycle.ViewModel
import io.reactivex.Flowable
import io.reactivex.Observable
import io.reactivex.Scheduler
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable
import io.reactivex.observers.DisposableObserver
import io.reactivex.schedulers.Schedulers


class TasksViewModel(val tasksRepository: TasksRepository) : ViewModel() {


    fun getTasks(): Flowable<List<Task>> {
        return tasksRepository.getTasks()
    }

}