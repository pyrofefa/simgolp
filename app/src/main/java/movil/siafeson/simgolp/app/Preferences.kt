package movil.siafeson.simgolp.app

import android.content.Context
import android.content.SharedPreferences

class Preferences(context: Context) {
    val PREFS_NAME = "movil.siafeson.simgolp"
    val prefs: SharedPreferences = context.getSharedPreferences(PREFS_NAME, 0)

    var userId: Int
        get() = prefs.getInt("user_id", 0)
        set(value) = prefs.edit().putInt("user_id",value).apply()

    var userName: String?
        get() = prefs.getString("user_name", "")
        set(value) = prefs.edit().putString("user_name", value).apply()

    var name: String?
        get() = prefs.getString("name", "")
        set(value) = prefs.edit().putString("name", value).apply()

    var lastName: String?
        get() = prefs.getString("last_name", "")
        set(value) = prefs.edit().putString("last_name",value).apply()

    var juntaId: Int
        get() = prefs.getInt("junta_id", 0)
        set(value) = prefs.edit().putInt("junta_id",value).apply()

    var personalId: Int
        get() = prefs.getInt("personal_id",0)
        set(value) = prefs.edit().putInt("personal_id",value).apply()

    var juntaSICAFIId: Int
        get() = prefs.getInt("junta_SICAFI_id",0)
        set(value) = prefs.edit().putInt("junta_SICAFI_id",value).apply()

    var junta: String?
        get() = prefs.getString("junta_name","")
        set(value) = prefs.edit().putString("junta_name",value).apply()

    var email: String?
        get() = prefs.getString("email","")
        set(value) = prefs.edit().putString("email",value).apply()

    var locationUpdate: Boolean
        get() = prefs.getBoolean("locations-update",false)
        set(value) = prefs.edit().putBoolean("locations-update",value).apply()

}