package apps.tim.pomos.feature.ui.tasks.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Flowable

@Dao
interface TaskDao {

    @Query("SELECT * FROM task")
    fun getAllTasks(): Flowable<List<Task>>

    @Insert(onConflict = REPLACE)
    fun insert(task: Task)
}