package apps.tim.pomos.feature.ui.tasks.data

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TasksRepository(private val taskDatabase: TaskDatabase) {
    private val taskDao = taskDatabase.taskDao()

    fun addPomo(id: Long) : Single<Unit> {
        return Single.fromCallable { taskDao.addPomodoro(id)  }
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun addTask(task: Task) {
        addToDB({ taskDao.insert(task) })
    }

    fun updateTitle(title: String, id: Long) {
        addToDB({ taskDao.updateTitle(title, id) })
    }

    fun getTasks(pos: Int?): Flowable<List<Task>>? {
        return taskDao.getTasksByDateRange()
    }

    fun deleteTask(task: Task) {
        addToDB({ taskDao.delete(task) })
    }

    fun completeTaskById(complete: Boolean, id: Long) {
        addToDB({taskDao.completeTaskById(complete, id)})
    }

    private fun addToDB(callable: () -> Unit) {
        Single.fromCallable(callable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

}