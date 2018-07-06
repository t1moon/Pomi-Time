package apps.tim.pomos.base.ui.base

import android.arch.lifecycle.ViewModel
import io.reactivex.disposables.CompositeDisposable

open class BaseViewModel : ViewModel() {
    protected val composite = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        composite.clear()
    }
}