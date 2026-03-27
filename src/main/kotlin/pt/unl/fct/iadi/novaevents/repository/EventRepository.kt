package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import pt.unl.fct.iadi.novaevents.domain.Event
import java.time.LocalDate

interface EventRepository : JpaRepository<Event, Long> {
    fun findByClubId(clubId: Long): List<Event>
    fun existsByNameIgnoreCase(name: String): Boolean

    /**
     *
     * """
     *     SELECT e FROM Event e
     *     WHERE (:type IS NULL OR e.type.name = :type)
     *       AND (:clubId IS NULL OR e.club.id = :clubId)
     *       AND (:from IS NULL OR e.date >= :from)
     *       AND (:to IS NULL OR e.date <= :to)
     * """
     *
     */
    @Query("""
    SELECT e FROM Event e
    JOIN FETCH e.club
    JOIN FETCH e.type
    WHERE (:type IS NULL OR e.type.name = :type)
      AND (:clubId IS NULL OR e.club.id = :clubId)
      AND (:from IS NULL OR e.date >= :from)
      AND (:to IS NULL OR e.date <= :to)
    """)
    fun findWithFilters(
        @Param("type") type: String?,
        @Param("clubId") clubId: Long?,
        @Param("from") from: LocalDate?,
        @Param("to") to: LocalDate?
    ): List<Event>

    fun countByClubId(clubId: Long): Int
}