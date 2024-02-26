package movil.siafeson.simgolp.utils

import android.app.Activity
import android.content.Context
import android.app.ProgressDialog
import android.content.Intent
import android.net.ConnectivityManager
import android.net.NetworkInfo
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import java.text.SimpleDateFormat
import java.util.*

fun isOnlineNet(context: Context): Boolean {
    val connectivityManager = context.getSystemService(Context.CONNECTIVITY_SERVICE)
    return if (connectivityManager is ConnectivityManager) {
        val networkInfo: NetworkInfo? = connectivityManager.activeNetworkInfo
        networkInfo?.isConnected ?: false
    } else false
}
inline fun <reified T : Activity> Fragment.goToActivity(noinline init: Intent.() -> Unit = {}){
    val intent = Intent(context, T::class.java)
    intent.init()
    startActivity(intent)
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
fun Date.fechaHoraSimple(): String{
    val hoy: String = SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(this)
    return hoy;
}
fun Date.fechaHoraCompleta(): String{
    val hoy: String = SimpleDateFormat("dd 'de' MMMM 'de' yyyy HH:mm:ss").format(this)
    return hoy;
}
//String
fun String.ucFirst(): String {
    if (this == null || this.isEmpty()) {
        return this;
    } else {
        return this.substring(0, 1).toUpperCase() + this.substring(1);
    }
}

fun showAlertDialog(
    title: String? = null,
    message: String? = null,
    context: Context,
    positiveButtonTitle: String = "",
    positiveButtonAction: (() -> Unit)? = null,
    negativeButtonTitle: String? = null,
    negativeButtonAction: (() -> Unit)? = null
) {
    val alertDialogBuilder = AlertDialog.Builder(context)

    // Configuración del diálogo
    alertDialogBuilder.apply {
        setTitle(title)
        setMessage(message)
        setPositiveButton(positiveButtonTitle) { _, _ ->
            positiveButtonAction?.invoke()
        }

        if (negativeButtonTitle != null) {
            setNegativeButton(negativeButtonTitle) { _, _ ->
                negativeButtonAction?.invoke()
            }
        }
    }

    // Crear y mostrar el diálogo
    alertDialogBuilder.create().show()
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
