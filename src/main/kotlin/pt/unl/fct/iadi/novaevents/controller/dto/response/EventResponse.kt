package pt.unl.fct.iadi.novaevents.controller.dto.response

import java.time.LocalDate

data class EventResponse(
    val id: Long,
    val clubId: Long,
    val clubName: String, // necessary?
    val name: String,
    val date: LocalDate,
    val location: String,
    val type: String,
    val description: String
) {
}