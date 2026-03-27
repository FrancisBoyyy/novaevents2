package pt.unl.fct.iadi.novaevents.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import org.springframework.data.jpa.repository.JpaRepository

@Entity
class EventType(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var name: String? = null
)

interface EventTypeRepository : JpaRepository<EventType, Long> {
    fun findByName(name: String): EventType?
    fun save(event: EventType): EventType?
}

/*
enum class EventType {
    WORKSHOP,
    TALK,
    COMPETITION,
    SOCIAL,
    MEETING,
    OTHER
}
 */