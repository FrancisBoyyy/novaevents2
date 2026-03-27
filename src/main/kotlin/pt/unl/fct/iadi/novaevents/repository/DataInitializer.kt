package pt.unl.fct.iadi.novaevents.config

import org.springframework.boot.ApplicationArguments
import org.springframework.boot.ApplicationRunner
import org.springframework.stereotype.Component
import pt.unl.fct.iadi.novaevents.domain.Club
import pt.unl.fct.iadi.novaevents.domain.Event
import pt.unl.fct.iadi.novaevents.domain.EventType
import pt.unl.fct.iadi.novaevents.domain.EventTypeRepository
import pt.unl.fct.iadi.novaevents.domain.enums.ClubCategory
import pt.unl.fct.iadi.novaevents.repository.ClubRepository
import pt.unl.fct.iadi.novaevents.repository.EventRepository
import java.time.LocalDate

@Component
class DataInitializer(
    private val eventTypeRepository: EventTypeRepository,
    private val clubRepository: ClubRepository,
    private val eventRepository: EventRepository
) : ApplicationRunner {

    override fun run(args: ApplicationArguments?) {
        if (eventTypeRepository.count() > 0 || clubRepository.count() > 0 || eventRepository.count() > 0) {
            return
        }

        val eventTypes = eventTypeRepository.saveAll(
            listOf(
                EventType(name = "WORKSHOP"),
                EventType(name = "TALK"),
                EventType(name = "COMPETITION"),
                EventType(name = "SOCIAL"),
                EventType(name = "MEETING"),
                EventType(name = "OTHER")
            )
        ).associateBy { it.name!! }

        val clubs = clubRepository.saveAll(
            listOf(
                Club(name = "Chess Club", description = "Chess Club", category = ClubCategory.ACADEMIC),
                Club(name = "Robotics Club", description = "The Robotics Club is the place to turn ideas into machines", category = ClubCategory.TECHNOLOGY),
                Club(name = "Photography Club", description = "Photography Club", category = ClubCategory.ARTS),
                Club(name = "Hiking & Outdoors Club", description = "Hiking & Outdoors Club", category = ClubCategory.SPORTS),
                Club(name = "Film Society", description = "Film Society", category = ClubCategory.CULTURAL)
            )
        ).associateBy { it.name!! }

        val events = listOf(
            Event(
                club = clubs["Chess Club"],
                name = "Beginner's Chess Workshop",
                date = LocalDate.parse("2026-03-10"),
                location = "Room A101",
                type = eventTypes["WORKSHOP"],
                description = "Beginner's Chess Workshop"
            ),
            Event(
                club = clubs["Chess Club"],
                name = "Spring Chess Tournament",
                date = LocalDate.parse("2026-04-05"),
                location = "Main Hall",
                type = eventTypes["COMPETITION"],
                description = "Spring Chess Tournament"
            ),
            Event(
                club = clubs["Chess Club"],
                name = "Advanced Openings Talk",
                date = LocalDate.parse("2026-05-20"),
                location = "Room A101",
                type = eventTypes["TALK"],
                description = "Advanced Openings Talk"
            ),

            Event(
                club = clubs["Robotics Club"],
                name = "Arduino Intro Workshop",
                date = LocalDate.parse("2026-03-15"),
                location = "Engineering Lab 2",
                type = eventTypes["WORKSHOP"],
                description = "Arduino Intro Workshop"
            ),
            Event(
                club = clubs["Robotics Club"],
                name = "RoboCup Preparation Meeting",
                date = LocalDate.parse("2026-03-28"),
                location = "Engineering Lab 1",
                type = eventTypes["MEETING"],
                description = "RoboCup Preparation Meeting"
            ),
            Event(
                club = clubs["Robotics Club"],
                name = "Sensor Integration Talk",
                date = LocalDate.parse("2026-04-22"),
                location = "Auditorium B",
                type = eventTypes["TALK"],
                description = "Sensor Integration Talk"
            ),

            Event(
                club = clubs["Photography Club"],
                name = "Night Photography Workshop",
                date = LocalDate.parse("2026-03-22"),
                location = "Campus Rooftop",
                type = eventTypes["WORKSHOP"],
                description = "Night Photography Workshop"
            ),
            Event(
                club = clubs["Photography Club"],
                name = "Portrait Photography Talk",
                date = LocalDate.parse("2026-04-14"),
                location = "Arts Studio 3",
                type = eventTypes["TALK"],
                description = "Portrait Photography Talk"
            ),
            Event(
                club = clubs["Photography Club"],
                name = "Photo Walk & Social",
                date = LocalDate.parse("2026-05-09"),
                location = "Main Entrance",
                type = eventTypes["SOCIAL"],
                description = "Photo Walk & Social"
            ),

            Event(
                club = clubs["Hiking & Outdoors Club"],
                name = "Serra da Arrábida Hike",
                date = LocalDate.parse("2026-03-29"),
                location = "Bus Stop Central",
                type = eventTypes["OTHER"],
                description = "Serra da Arrábida Hike"
            ),
            Event(
                club = clubs["Hiking & Outdoors Club"],
                name = "Trail Safety Workshop",
                date = LocalDate.parse("2026-04-08"),
                location = "Room C205",
                type = eventTypes["WORKSHOP"],
                description = "Trail Safety Workshop"
            ),
            Event(
                club = clubs["Hiking & Outdoors Club"],
                name = "Spring Camping Trip",
                date = LocalDate.parse("2026-05-15"),
                location = "Bus Stop Central",
                type = eventTypes["SOCIAL"],
                description = "Spring Camping Trip"
            ),

            Event(
                club = clubs["Film Society"],
                name = "Kubrick Retrospective Screening",
                date = LocalDate.parse("2026-03-18"),
                location = "Cinema Room",
                type = eventTypes["SOCIAL"],
                description = "Kubrick Retrospective Screening"
            ),
            Event(
                club = clubs["Film Society"],
                name = "Screenwriting Workshop",
                date = LocalDate.parse("2026-04-30"),
                location = "Arts Studio 1",
                type = eventTypes["WORKSHOP"],
                description = "Screenwriting Workshop"
            )
        )

        eventRepository.saveAll(events)
    }
}