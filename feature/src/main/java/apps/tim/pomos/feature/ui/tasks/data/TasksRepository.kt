package apps.tim.pomos.feature.ui.tasks.data

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TasksRepository(private val taskDatabase: TaskDatabase) {

    fun addTask(task: Task) {
        Single.fromCallable { taskDatabase.taskDao().insert(task) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun updateTitle(title: String, id: Long) {
        Single.fromCallable { taskDatabase.taskDao().updateTitle(title, id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun getTasks(pos: Int?): Flowable<List<Task>>? {
        return taskDatabase.taskDao().getTasksByDateRange()
    }

    fun deleteTask(task: Task) {
        Single.fromCallable { taskDatabase.taskDao().delete(task) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun completeTaskById(complete: Boolean, id: Long) {
        Single.fromCallable { taskDatabase.taskDao().compeleTaskById(complete, id) }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}