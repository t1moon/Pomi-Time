package apps.tim.pomos.base.data.repository

import apps.tim.pomos.base.data.TaskDatabase
import apps.tim.pomos.base.data.entity.Statistics
import apps.tim.pomos.base.data.entity.Task
import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TasksRepository(taskDatabase: TaskDatabase) : ITasksRepository {
    private val taskDao = taskDatabase.taskDao()
    private val statDao = taskDatabase.statDao()

    override fun getTasks(): Flowable<List<Task>> {
        return taskDao.getTasksByDateRange()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun getStats(): Flowable<List<Statistics>> {
        return statDao.getLastStats()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
    }

    override fun addPomo(id: Long) =
            completable { taskDao.addPomodoro(id) }


    override fun addTask(task: Task) =
            completable { taskDao.insert(task) }


    override fun deleteTask(task: Task) =
            completable { taskDao.delete(task) }


    override fun setCompleteTaskById(complete: Boolean, id: Long) =
            completable { taskDao.completeTaskById(complete, id) }


    override fun activateTask(id: Long) =
            completable { taskDao.activateTask(id) }


    override fun moveActiveTasksToBacklog() =
            completable { taskDao.moveActiveTasksToBacklog() }


    override fun deleteCompletedTasks() =
            completable { taskDao.deleteCompletedTasks() }


    override fun addStatistics(stat: Statistics) =
            completable { statDao.insert(stat) }


    private fun completable(callable: () -> Unit): Completable =
            Completable.fromCallable(callable)
                    .subscribeOn(Schedulers.io())
                    .observeOn(AndroidSchedulers.mainThread())
}