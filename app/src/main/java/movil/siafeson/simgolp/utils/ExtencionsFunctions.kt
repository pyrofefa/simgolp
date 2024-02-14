package movil.siafeson.simgolp.utils

import android.app.Activity
import android.content.Context
import android.app.ProgressDialog
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AlertDialog
import java.text.SimpleDateFormat
import java.util.*

fun Activity.isOnlineNet(): Boolean {
    val connectivityManager = this.getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}
fun Calendar.nombreDiaActual() :String {
    val espanol = Locale("es", "MX")
    val date = this.time
    val fecha: String = SimpleDateFormat("EEEE", espanol).format(date).toString().ucFirst()
    return fecha
}
fun Calendar.fechaCompleta(): String{
    val espanol = Locale("es", "MX")
    val date = this.time
    val fecha: String = SimpleDateFormat("dd 'de' MMMM 'de' yyyy", espanol).format(date)
    return fecha
}
//String
fun String.ucFirst(): String {
    if (this == null || this.isEmpty()) {
        return this;
    } else {
        return this.substring(0, 1).toUpperCase() + this.substring(1);
    }
}

fun alertDialog(
    title: String,
    message: String,
    actionText: String,
    context: Context,
) {
    val builder = AlertDialog.Builder(context)
    builder.setTitle(title)
    builder.setMessage(message)
    builder.setPositiveButton(actionText,null)
    val alertDialog: AlertDialog = builder.create()
    alertDialog.show()
}


fun showProgressDialog(title: String, message: String, context: Context, style: Int): ProgressDialog {
    val progressDialog = ProgressDialog(context)
    progressDialog.setTitle(title)
    progressDialog.setMessage(message)
    progressDialog.setProgressStyle(style)
    progressDialog.setCancelable(false)
    progressDialog.show()
    return progressDialog
}
