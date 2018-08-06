package apps.tim.pomos.base.ui.base

import android.support.v4.app.DialogFragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import io.reactivex.disposables.CompositeDisposable

open class BaseDialogFragment : DialogFragment() {
    protected val compositeDisposable = CompositeDisposable()

    fun ViewGroup.inflate(layoutRes: Int): View {
        return LayoutInflater.from(context).inflate(layoutRes, this, false)
    }
    override fun onDestroyView() {
        compositeDisposable.clear()
        super.onDestroyView()
    }

    protected fun showError(t: Throwable) {
        activity?.showError()
    }
}