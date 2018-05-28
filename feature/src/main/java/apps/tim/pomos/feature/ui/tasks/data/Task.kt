package apps.tim.pomos.feature.ui.tasks.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey

@Entity
data class Task(
        @PrimaryKey(autoGenerate = true) val id: Long?,
        @ColumnInfo val title: String
)