package apps.tim.pomos.base.data.dao

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import apps.tim.pomos.base.data.entity.Task
import io.reactivex.Flowable

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getTasksByDateRange(): Flowable<List<Task>>

    @Insert(onConflict = REPLACE)
    fun insert(task: Task)

    @Query("UPDATE task SET pomo = pomo + 1, currentPomo = currentPomo + 1 WHERE id =:id")
    fun addPomodoro(id: Long)

    @Delete
    fun delete(task: Task)

    @Query("UPDATE task SET isComplete = :isComplete WHERE id =:id")
    fun completeTaskById(isComplete: Boolean, id: Long)

    @Query("UPDATE task SET isActive = 1 WHERE id =:id")
    fun activateTask(id: Long)

    @Query("UPDATE task SET currentPomo = 0 WHERE id =:id")
    fun resetCurrentPomo(id: Long)

    @Query("UPDATE task set isActive = 0, currentPomo = 0 WHERE isActive = 1 AND isComplete = 0")
    fun moveActiveTasksToBacklog()

    @Query("DELETE FROM task WHERE isComplete = 1")
    fun deleteCompletedTasks()

}