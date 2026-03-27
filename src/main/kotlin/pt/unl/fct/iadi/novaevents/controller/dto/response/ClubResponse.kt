package pt.unl.fct.iadi.novaevents.controller.dto.response

import pt.unl.fct.iadi.novaevents.domain.enums.ClubCategory

data class ClubResponse(
    val id: Long,
    val name: String,
    val description: String,
    val category: ClubCategory,
    val eventCount: Int
) {
}