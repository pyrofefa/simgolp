package movil.siafeson.simgolp.models

data class LocationsResponse (
    var status: String,
    var message: String,
    var count : Int,
    var data: List<LocationData>,
    var log: Any?
)

