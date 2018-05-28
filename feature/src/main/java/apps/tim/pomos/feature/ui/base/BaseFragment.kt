package apps.tim.pomos.feature.ui.base

import android.support.v4.app.Fragment
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment: Fragment() {
    protected val compositeDisposable = CompositeDisposable()


    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }
}