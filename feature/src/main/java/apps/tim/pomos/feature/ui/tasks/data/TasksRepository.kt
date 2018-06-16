package apps.tim.pomos.feature.ui.tasks.data

import apps.tim.pomos.feature.PomoApp
import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TasksRepository {

    fun addTask(task: Task) {
        Single.fromCallable { PomoApp.database?.taskDao()?.insert(task) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun updateTitle(title: String, id: Long) {
        Single.fromCallable { PomoApp.database?.taskDao()?.updateTitle(title, id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun getTasks(pos: Int?): Flowable<List<Task>>? {
        return PomoApp.database?.taskDao()?.getTasksByDateRange()
    }

    fun deleteTask(task: Task) {
        Single.fromCallable { PomoApp.database?.taskDao()?.delete(task) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun completeTaskById(complete: Boolean, id: Long) {
        Single.fromCallable { PomoApp.database?.taskDao()?.compeleTaskById(complete, id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}