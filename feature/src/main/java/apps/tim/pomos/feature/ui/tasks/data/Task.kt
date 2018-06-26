package apps.tim.pomos.feature.ui.tasks.data

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import apps.tim.pomos.feature.ui.DEFAULT_DATE_LONG
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Task(
        @PrimaryKey(autoGenerate = true) var id: Long,
        @ColumnInfo var title: String,
        @ColumnInfo var pomo: Int = 0,
        @ColumnInfo var currentPomo: Int = 0,
        @ColumnInfo var deadline: Long = DEFAULT_DATE_LONG,
        @ColumnInfo var isComplete: Boolean = false,
        @ColumnInfo var isActive: Boolean = false
) : Parcelable