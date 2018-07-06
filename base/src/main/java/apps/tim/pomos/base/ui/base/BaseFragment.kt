package apps.tim.pomos.base.ui.base

import android.content.Context
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import io.reactivex.disposables.CompositeDisposable

open class BaseFragment: Fragment() {
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

    fun Fragment.hideSoftKeyboard() {
        val imm = activity?.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(view?.windowToken, 0)
    }
}