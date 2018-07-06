package apps.tim.pomos.base.data.entity

import android.arch.persistence.room.ColumnInfo
import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.os.Parcelable
import apps.tim.pomos.base.DEFAULT_DATE_LONG
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity
data class Task(
        @PrimaryKey(autoGenerate = true) var id: Long,
        @ColumnInfo var title: String,
        @ColumnInfo var pomo: Int = 0,
        @ColumnInfo var currentPomo: Int = 0,
        @ColumnInfo var deadline: Long = DEFAULT_DATE_LONG,
        @ColumnInfo var created: Long = DEFAULT_DATE_LONG,
        @ColumnInfo var isComplete: Boolean = false,
        @ColumnInfo var isActive: Boolean = false
) : Parcelable