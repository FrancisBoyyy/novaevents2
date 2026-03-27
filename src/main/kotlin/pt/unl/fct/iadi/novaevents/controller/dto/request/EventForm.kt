package pt.unl.fct.iadi.novaevents.controller.dto.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class EventForm(
    @field:NotBlank(message = "Name is required")
    val name: String = "",

    @field:NotNull(message = "Date is required")
    val date: LocalDate? = null,

    val location: String = "",

    @field:NotNull(message = "Event type is required")
    val type: String? = null,

    val description: String = "",
) {
}