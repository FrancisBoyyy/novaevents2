package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.request.EventForm
import pt.unl.fct.iadi.novaevents.controller.dto.response.ClubResponse
import pt.unl.fct.iadi.novaevents.controller.dto.response.DetailedClubResponse
import pt.unl.fct.iadi.novaevents.controller.dto.response.EventResponse
import pt.unl.fct.iadi.novaevents.domain.Event
import pt.unl.fct.iadi.novaevents.domain.EventType
import pt.unl.fct.iadi.novaevents.domain.EventTypeRepository
import pt.unl.fct.iadi.novaevents.repository.ClubRepository
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import java.time.LocalDate

@Service
class NovaEventsService(
    private val clubRepository: ClubRepository,
    private val eventRepository: EventRepository,
    private val eventTypeRepository: EventTypeRepository
) {
    fun getAllClubs() : List<ClubResponse> {
        val clubs = clubRepository.findAll()

        return clubs.map {
            club -> ClubResponse(
                id = club.id!!,
                name = club.name!!,
                description = club.description!!,
                category = club.category!!,
                eventCount = eventRepository.countByClubId(club.id!!),
            )
        }
    }

    fun getClubDetails(clubId: Long) : DetailedClubResponse {
        val club = clubRepository.findById(clubId)
            .orElseThrow { NoSuchElementException("Club not found") }

        val events = eventRepository.findByClubId(clubId)

        return DetailedClubResponse(
            id = club.id!!,
            name = club.name!!,
            description = club.description!!,
            category = club.category!!,
            events = events.map { it.toResponse(club.name!!) }
        )
    }

    fun getAllEvents(
        type: EventType? = null,
        clubId: Long? = null,
        from: LocalDate? = null,
        to: LocalDate? = null
    ) : List<EventResponse> {
        val events = eventRepository.findWithFilters(type?.name, clubId, from, to)

        return events.map { event ->
            EventResponse(
                id = event.id!!,
                clubId = event.club!!.id!!,
                clubName = event.club!!.name!!,
                name = event.name!!,
                date = event.date!!,
                location = event.location!!,
                type = event.type!!.name!!,
                description = event.description!!
            )
        }
    }

    fun getEventDetails(clubId: Long, eventId: Long): EventResponse {
        val club = clubRepository.findById(clubId)
            .orElseThrow { NoSuchElementException("Club not found") }

        val event = eventRepository.findById(eventId)
            .orElseThrow { NoSuchElementException("Event not found") }

        return EventResponse(
            id = event.id!!,
            clubId = event.club!!.id!!,
            clubName = club.name!!,
            name = event.name!!,
            date = event.date!!,
            location = event.location!!,
            type = event.type!!.name!!,
            description = event.description!!
        )
    }

    fun createEvent(clubId: Long, eventForm: EventForm): EventResponse {
        val club = clubRepository.findById(clubId)
            .orElseThrow { NoSuchElementException("Club not found") }

        val type = eventTypeRepository.findByName(eventForm.type!!)
            ?: eventTypeRepository.save(EventType(name = eventForm.type!!))

        if (eventRepository.existsByNameIgnoreCase(eventForm.name)) {
            throw IllegalArgumentException("An event with this name already exists")
        }

        val event = Event(
            club = club,
            name = eventForm.name,
            date = eventForm.date!!,
            location = eventForm.location ?: "",
            type = type,
            description = eventForm.description ?: ""
        )

        val saved = eventRepository.save(event)
        return saved.toResponse(club.name!!)
    }

    fun editEvent(clubId: Long, eventId: Long, eventForm: EventForm): EventResponse {
        val club = clubRepository.findById(clubId)
            .orElseThrow { NoSuchElementException("Club not found") }

        val existingEvent = eventRepository.findById(eventId)
            .orElseThrow { NoSuchElementException("Event not found") }

        val type = eventTypeRepository.findByName(eventForm.type!!)
            ?: eventTypeRepository.save(EventType(name = eventForm.type!!))

        if (eventRepository.existsByNameIgnoreCase(eventForm.name)) {
            throw IllegalArgumentException("An event with this name already exists")
        }

        val updated = Event(
            id = eventId,
            club = club,
            name = eventForm.name ?: existingEvent.name,
            date = eventForm.date ?: existingEvent.date,
            location = eventForm.location ?: existingEvent.location,
            type = type,
            description = eventForm.description ?: existingEvent.description
        )

        val saved = eventRepository.save(updated)
        return saved.toResponse(club.name!!)
    }

    fun deleteEvent(clubId: Long, eventId: Long): Long {

        val club = clubRepository.findById(clubId)
            .orElseThrow { NoSuchElementException("Club not found") }

        val event = eventRepository.findById(eventId)
            .orElseThrow { NoSuchElementException("Event not found") }

        eventRepository.delete(event)

        return club.id?: clubId
    }

    fun getEventTypes(): List<EventType> {
        val eventTypes = eventTypeRepository.findAll()
        return eventTypes
    }

    private fun Event.toResponse(clubName: String) =
        EventResponse(
            id = id!!,
            clubId = club!!.id!!,
            clubName = clubName,
            name = name!!,
            date = date!!,
            location = location?: "",
            type = type!!.name!!,
            description = description?: ""
        )
}