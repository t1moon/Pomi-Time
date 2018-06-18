package apps.tim.pomos.feature.ui.base

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.disposables.Disposable

open class BaseFragment: Fragment() {
    protected val compositeDisposable = CompositeDisposable()


    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }

    protected fun add(d: Disposable) {
        compositeDisposable.add(d)
    }
}