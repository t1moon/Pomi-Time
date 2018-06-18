package apps.tim.pomos.feature.ui.tasks.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Task(
        @PrimaryKey(autoGenerate = true) var id: Long,
        @ColumnInfo var title: String,
        @ColumnInfo var complete: Boolean = false,
        @ColumnInfo var pomodoros: Int = 0
) : Parcelable