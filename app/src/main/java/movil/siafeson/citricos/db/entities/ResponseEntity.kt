package movil.siafeson.citricos.db.entities

data class ResponseEntity (
    val status: String,
    val message: String,
    val data: Any?,
    val log: String
)
