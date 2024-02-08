package movil.siafeson.simgolp.App

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {
    val PREFS_NAME = "movil.siafeson.simgolp"
    val SHARED_NAME = "auth-token-simgolp"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)
    var userName: String?
        get() = prefs.getString(SHARED_NAME, "")
        set(value) = prefs.edit().putString(SHARED_NAME, value).apply()
}