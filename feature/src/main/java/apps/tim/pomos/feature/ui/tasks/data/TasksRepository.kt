package apps.tim.pomos.feature.ui.tasks.data

import io.reactivex.Flowable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers


class TasksRepository(taskDatabase: TaskDatabase) {
    private val taskDao = taskDatabase.taskDao()
    private val statDao = taskDatabase.statDao()

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

    fun getTasks(): Flowable<List<Task>> {
        return taskDao.getTasksByDateRange()
    }

    fun getStats(): Flowable<List<Statistics>> {
        return statDao.getLastStats()
    }

    fun deleteTask(task: Task) {
        addToDB({ taskDao.delete(task) })
    }

    fun completeTaskById(complete: Boolean, id: Long) {
        addToDB { taskDao.completeTaskById(complete, id) }
    }

    private fun addToDB(callable: () -> Unit) {
        Single.fromCallable(callable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }

    fun activateTask(id: Long) {
        addToDB {
            taskDao.activateTask(id)
        }
    }

    fun deactivateTask(id: Long) {
        addToDB {
            taskDao.activateTask(id)
            taskDao.resetCurrentPomo(id)
        }
    }

    fun moveActiveTasksToBacklog() {
        addToDB {
            taskDao.moveActiveTasksToBacklog()
        }
    }

    fun deleteCompletedTasks() {
        addToDB {
            taskDao.deleteCompletedTasks()
        }
    }

    fun addStatistics(stat: Statistics) {
        addToDB {
            statDao.insert(stat)
        }
    }
}