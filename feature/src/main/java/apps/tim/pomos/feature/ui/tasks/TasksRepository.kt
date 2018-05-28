package apps.tim.pomos.feature.ui.tasks

import io.reactivex.Flowable
import io.reactivex.Observable


class TasksRepository {
    fun getTasks(): Flowable<List<Task>> {
        return Flowable.just(listOf(Task("First"), Task("Second")))
    }
}