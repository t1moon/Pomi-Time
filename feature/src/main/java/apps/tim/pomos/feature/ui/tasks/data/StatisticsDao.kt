package apps.tim.pomos.feature.ui.tasks.data

import android.arch.persistence.room.Dao
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE


@Dao
interface StatisticsDao {

    @Insert(onConflict = REPLACE)
    fun insert(stat: Statistics)
}
