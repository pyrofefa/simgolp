package movil.siafeson.citricos.models

data class RequestObject(
    val muestreo: List<RecordData>?,
    val pt_ind: String,
    val pt_lan: String,
    val pt_lon: String,
    val pt_dist: String,
    val pt_fecha: String,
    val pt_adultos: String
)
