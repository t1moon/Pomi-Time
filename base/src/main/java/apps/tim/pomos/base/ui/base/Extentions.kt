package apps.tim.pomos.base.ui.base

import android.app.Activity
import android.widget.Toast
import apps.tim.pomos.base.R
import apps.tim.pomos.base.app.PomoApp

fun Activity.showError() {
    Toast.makeText(this, PomoApp.string(R.string.error_text), Toast.LENGTH_SHORT).show()
}
