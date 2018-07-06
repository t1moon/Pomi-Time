package apps.tim.pomos.base.data

import io.reactivex.Completable
import io.reactivex.Flowable
import io.reactivex.Single

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