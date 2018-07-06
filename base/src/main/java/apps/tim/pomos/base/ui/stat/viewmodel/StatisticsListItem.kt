package apps.tim.pomos.base.ui.stat.viewmodel


class StatisticsListItem(
        val title: String,
        val pomo: Int,
        val deadline: Long,
        val isComplete: Boolean,
        val completePercentage: Int
        )