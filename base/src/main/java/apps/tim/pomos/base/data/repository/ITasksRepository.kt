package apps.tim.pomos.base.data.repository

import apps.tim.pomos.base.data.entity.Statistics
import apps.tim.pomos.base.data.entity.Task
import io.reactivex.Completable
import io.reactivex.Flowable

interface ITasksRepository {
    fun getTasks(): Flowable<List<Task>>
    fun getStats(): Flowable<List<Statistics>>

    fun addStatistics(stat: Statistics): Completable
    fun addPomo(id: Long) : Completable
    fun addTask(task: Task): Completable

    fun setCompleteTaskById(complete: Boolean, id: Long): Completable
    fun activateTask(id: Long): Completable
    fun moveActiveTasksToBacklog(): Completable
    fun deleteCompletedTasks(): Completable
    fun deleteTask(task: Task): Completable

}