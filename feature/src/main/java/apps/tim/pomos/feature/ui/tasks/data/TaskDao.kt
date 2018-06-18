package apps.tim.pomos.feature.ui.tasks.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getTasksByDateRange(): Flowable<List<Task>>

    @Insert(onConflict = REPLACE)
    fun insert(task: Task)

    @Query("UPDATE task SET title = :title WHERE id =:id")
    fun updateTitle(title: String?, id: Long)

    @Query("UPDATE task SET pomodoros = pomodoros + 1 WHERE id =:id")
    fun addPomodoro(id: Long)

    @Delete
    fun delete(task: Task)

    @Query("UPDATE task SET complete = :complete WHERE id =:id")
    fun completeTaskById(complete: Boolean, id: Long)
}