package apps.tim.pomos.feature.ui.picker

import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import apps.tim.pomos.feature.R
import kotlinx.android.synthetic.main.fragment_settings.*
import kotlinx.android.synthetic.main.fragment_tasks_tabs.*

class PickerFragment : Fragment() {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_picker, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        notificationsFragmentLabel.text = "Picker"
    }
}