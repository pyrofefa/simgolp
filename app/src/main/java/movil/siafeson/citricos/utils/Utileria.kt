package movil.siafeson.citricos.utils

import android.content.Context
import android.view.Gravity
import android.widget.Toast

class Utileria {

    fun getOrien(bear: Float): String {
        var orien = ""
        if (bear > -22.5 && bear <= 0 || bear > 0 && bear <= 22.5) {
            orien = "N"
        } else if (bear > 22.5 && bear <= 67.5) {
            orien = "NE"
        } else if (bear > 67.5 && bear <= 112.5) {
            orien = "E"
        } else if (bear > 112.5 && bear <= 157.5) {
            orien = "SE"
        } else if (bear > 157.5 && bear <= 180 || bear < -157.5 && bear >= -180) {
            orien = "S"
        } else if (bear > -157.5 && bear <= -112.5) {
            orien = "SW"
        } else if (bear > -112.5 && bear <= -67.5) {
            orien = "W"
        } else if (bear > -67.5 && bear <= -22.5) {
            orien = "NW"
        }
        return orien
    }

    fun obtenerLabelFecha(ano: Int?, mes: String, dia: String): String {
        return dia + " de " + obtenerMesName(mes) + " de " + ano
    }
    fun obtenerMesName(mes: String): String {
        var name = ""
        when (mes) {
            "01" -> name = "Enero"
            "02" -> name = "Febrero"
            "03" -> name = "Marzo"
            "04" -> name = "Abril"
            "05" -> name = "Mayo"
            "06" -> name = "Junio"
            "07" -> name = "Julio"
            "08" -> name = "Agosto"
            "09" -> name = "Septiembre"
            "10" -> name = "Octubre"
            "11" -> name = "Noviembre"
            "12" -> name = "Diciembre"
        }
        return name
    }

    fun myToasCustom(context: Context, message: CharSequence, duration: Int = Toast.LENGTH_SHORT, center: Boolean){
        val t = Toast.makeText(context, message, duration)
        if(center) t.setGravity(Gravity.CENTER_VERTICAL or Gravity.CENTER_HORIZONTAL, 0, 0)
        t.show()
    }
}