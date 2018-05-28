package apps.tim.pomos.feature.ui.tasks.data

import apps.tim.pomos.feature.ui.tasks.data.Task
import io.reactivex.Flowable


class TasksRepository {
    fun getTasks(pos: Int?): Flowable<List<Task>> {
        return Flowable.just(listOf(Task("First $pos"), Task("Second $pos")))
    }
}