package movil.siafeson.citricos.models

data class LocationsResponse (
    var status: String,
    var message: String,
    var count : Int,
    var data: List<LocationData>,
    var log: Any?
)

