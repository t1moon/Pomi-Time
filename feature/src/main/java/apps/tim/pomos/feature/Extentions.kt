package apps.tim.pomos.feature

import android.content.res.Resources
import android.util.TypedValue
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import java.util.concurrent.Callable


fun Resources.dipToPx(dip: Float) : Float {
    return TypedValue.applyDimension(
            TypedValue.COMPLEX_UNIT_DIP, dip, this.displayMetrics)
}

fun Single<Unit>.executeOnDB(callable: Callable<Unit>) {
    Single.fromCallable {
        Single.fromCallable(callable)
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe()
    }
}