package rohitrj.com.event.Activities

import android.app.Application
import com.google.firebase.database.FirebaseDatabase

class Offline: Application() {

    override fun onCreate() {
        super.onCreate()
        FirebaseDatabase.getInstance().setPersistenceEnabled(true)
    }
}