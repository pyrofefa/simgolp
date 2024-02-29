package movil.siafeson.citricos.utils

import android.app.Activity
import android.content.Context
import android.app.ProgressDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkInfo
import android.widget.Toast
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

fun Context.getAppVersionInfo(): Pair<String, Int> {
    try {
        val packageInfo = packageManager.getPackageInfo(packageName, 0)
        val versionName = packageInfo.versionName
        val versionCode = packageInfo.versionCode
        return Pair(versionName, versionCode)
    } catch (e: PackageManager.NameNotFoundException) {
        // Manejar la excepción según tus necesidades
        e.printStackTrace()
    }
    return Pair("", 0)
}

fun Calendar.getYear(): Int = get(Calendar.YEAR)

fun Calendar.getWeek(): Int = get(Calendar.WEEK_OF_YEAR)

fun Calendar.getFormattedDate(): String {
    val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
    return dateFormat.format(time)
}

fun Calendar.getFormattedDateTime(): String {
    val dateTimeFormat = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    return dateTimeFormat.format(time)
}

fun parseFecha(inputFecha: String): String {
    // Formato de entrada
    val formatoEntrada = SimpleDateFormat("dd 'de' MMMM 'de' yyyy HH:mm:ss", Locale.getDefault())
    // Parsear la cadena en un objeto Date
    val fecha = formatoEntrada.parse(inputFecha)
    // Formato de salida
    val formatoSalida = SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())
    // Formatear la fecha al formato deseado
    return fecha?.let { formatoSalida.format(it) } ?: ""
}

fun showToast(context: Context, mensaje: String) {
    Toast.makeText(context, mensaje, Toast.LENGTH_SHORT).show()
}

fun calculatePointsRequired(has: Int): Int {
    return when {
        has == 0 -> 1
        has > 0 && has < 6.25 -> 40
        has >= 6.25 && has < 26.25 -> 100
        has >= 26.25 && has < 50.25 -> 140
        has >= 50.25 && has < 75.25 -> 170
        has >= 75.25 -> 200
        else -> 0
    }
}