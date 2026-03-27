package pt.unl.fct.iadi.novaevents.service

import org.springframework.stereotype.Service
import pt.unl.fct.iadi.novaevents.controller.dto.request.EventForm
import pt.unl.fct.iadi.novaevents.controller.dto.response.DetailedClubResponse
import pt.unl.fct.iadi.novaevents.controller.dto.response.EventResponse
import pt.unl.fct.iadi.novaevents.domain.Club
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
    /**
    private val clubs = listOf<Club>(
        Club(
            1,
            "Chess Club",
            "Chess Club",
            ClubCategory.ACADEMIC
        ),
        Club(
            2,
            "Robotics Club",
            "The Robotics Club is the place to turn ideas into machines",
            ClubCategory.TECHNOLOGY
        ),
        Club(
            3,
            "Photography Club",
            "Photography Club",
            ClubCategory.ARTS
        ),
        Club(
            4,
            "Hiking & Outdoors Club",
            "Hiking & Outdoors Club",
            ClubCategory.SPORTS
        ),
        Club(
            5,
            "Film Society",
            "Film Society",
            ClubCategory.CULTURAL
        )
    )

    private val events = ConcurrentHashMap<Long, ConcurrentHashMap<Long, Event>>(5)
    private var nextEventId = 1L
    private val formatter = DateTimeFormatter.ofPattern("dd MMM yyyy")

    init {
        // Chess Club
        addEvent(1L, "Beginner's Chess Workshop", "10 Mar 2026", "Room A101", EventType.WORKSHOP)
        addEvent(1L, "Spring Chess Tournament", "05 Apr 2026", "Main Hall", EventType.COMPETITION)
        addEvent(1L, "Advanced Openings Talk", "20 May 2026", "Room A101", EventType.TALK)

        // Robotics Club
        addEvent(2L, "Arduino Intro Workshop", "15 Mar 2026", "Engineering Lab 2", EventType.WORKSHOP)
        addEvent(2L, "RoboCup Preparation Meeting", "28 Mar 2026", "Engineering Lab 1", EventType.MEETING)
        addEvent(2L, "Sensor Integration Talk", "22 Apr 2026", "Auditorium B", EventType.TALK)
        addEvent(2L, "Regional Robotics Competition", "01 Jun 2026", "Sports Hall", EventType.COMPETITION)

        // Photography Club
        addEvent(3L, "Night Photography Workshop", "22 Mar 2026", "Campus Rooftop", EventType.WORKSHOP)
        addEvent(3L, "Portrait Photography Talk", "14 Apr 2026", "Arts Studio 3", EventType.TALK)
        addEvent(3L, "Photo Walk & Social", "09 May 2026", "Main Entrance", EventType.SOCIAL)

        // Hiking & Outdoors Club
        addEvent(4L, "Serra da Arrábida Hike", "29 Mar 2026", "Bus Stop Central", EventType.OTHER)
        addEvent(4L, "Trail Safety Workshop", "08 Apr 2026", "Room C205", EventType.WORKSHOP)
        addEvent(4L, "Spring Camping Trip", "15 May 2026", "Bus Stop Central", EventType.SOCIAL)

        // Film Society
        addEvent(5L, "Kubrick Retrospective Screening", "18 Mar 2026", "Cinema Room", EventType.SOCIAL)
        addEvent(5L, "Screenwriting Workshop", "30 Apr 2026", "Arts Studio 1", EventType.WORKSHOP)
    }

    private fun addEvent(clubId: Long, name: String, dateStr: String, location: String, type: EventType) {
        // Ensure the club's inner map exists
        val clubEvents = events.computeIfAbsent(clubId) { ConcurrentHashMap() }

        // Create the event
        val event = Event(
            id = nextEventId++,
            clubId = clubId,
            name = name,
            date = LocalDate.parse(dateStr, formatter),
            location = location,
            type = type,
            description = name
        )

        // Put it in the inner map with the event ID as key
        clubEvents[event.id] = event
    }
    */

    fun getAllClubs() : List<Club> {
        return clubRepository.findAll()
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