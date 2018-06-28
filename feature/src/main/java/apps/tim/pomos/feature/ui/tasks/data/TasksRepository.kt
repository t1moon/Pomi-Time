package apps.tim.pomos.feature.ui.tasks.data

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TasksRepository(taskDatabase: TaskDatabase) {
    private val taskDao = taskDatabase.taskDao()
    private val statDao = taskDatabase.statDao()

    fun addPomo(id: Long) : Single<Unit> {
        return async { taskDao.addPomodoro(id)  }
    }

    fun addTask(task: Task): Single<Unit> {
        return async { taskDao.insert(task) }
    }

    fun getTasks(): Flowable<List<Task>> {
        return taskDao.getTasksByDateRange()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun getStats(): Flowable<List<Statistics>> {
        return statDao.getLastStats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    fun deleteTask(task: Task): Single<Unit> {
        return async { taskDao.delete(task) }
    }

    fun completeTaskById(complete: Boolean, id: Long): Single<Unit> {
        return async { taskDao.completeTaskById(complete, id) }
    }



    fun activateTask(id: Long): Single<Unit> {
        return async { taskDao.activateTask(id) }
    }

    fun moveActiveTasksToBacklog(): Single<Unit> {
        return async { taskDao.moveActiveTasksToBacklog() }
    }

    fun deleteCompletedTasks(): Single<Unit> {
        return async { taskDao.deleteCompletedTasks() }
    }

    fun addStatistics(stat: Statistics): Single<Unit> {
        return async { statDao.insert(stat) }
    }

    private fun async(callable: () -> Unit) : Single<Unit> {
        return Single.fromCallable(callable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }
}