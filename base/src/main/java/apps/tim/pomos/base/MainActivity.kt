package apps.tim.pomos.base

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import androidx.navigation.findNavController

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }

    override fun onSupportNavigateUp() =
            findNavController(R.id.mainNavigationFragment).navigateUp()
}
