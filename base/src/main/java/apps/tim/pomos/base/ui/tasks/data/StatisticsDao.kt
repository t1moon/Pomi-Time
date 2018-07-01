package apps.tim.pomos.base.ui.tasks.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query
import io.reactivex.Flowable


@Dao
interface StatisticsDao {

    @Insert(onConflict = REPLACE)
    fun insert(stat: Statistics)

    @Query("SELECT * FROM statistics")
    fun getLastStats(): Flowable<List<Statistics>>
}
