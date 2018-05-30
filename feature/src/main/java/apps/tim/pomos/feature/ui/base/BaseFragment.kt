package apps.tim.pomos.feature.ui.base

import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment: Fragment() {
    protected val compositeDisposable = CompositeDisposable()


    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
}