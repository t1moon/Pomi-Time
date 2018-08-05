package apps.tim.pomos.base.ui.base

import android.os.Bundle
import android.support.v4.app.DialogFragment
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatDialog
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import io.reactivex.disposables.CompositeDisposable

abstract class BaseDialogFragment : DialogFragment() {
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

    abstract fun getLayoutRes() : Int

    override fun onCreateDialog(savedInstanceState: Bundle?): AppCompatDialog {
        val builder = AlertDialog.Builder(context!!)
        val view = activity?.layoutInflater?.inflate(getLayoutRes(), null)
        view?.let {
            initView(it)
        }
        builder.setView(view)
        val dialog = builder.create()
        dialog.window.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        return dialog
    }

    abstract fun initView(it: View)
}