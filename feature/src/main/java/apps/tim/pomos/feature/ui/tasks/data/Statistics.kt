package apps.tim.pomos.feature.ui.tasks.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Statistics(
        @PrimaryKey(autoGenerate = true) var id: Long,
        @ColumnInfo var date: Long,
        @ColumnInfo var completed: Int = 0
)
