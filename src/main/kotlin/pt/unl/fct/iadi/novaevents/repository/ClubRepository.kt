package pt.unl.fct.iadi.novaevents.repository

import org.springframework.data.jpa.repository.JpaRepository
import pt.unl.fct.iadi.novaevents.domain.Club

interface ClubRepository : JpaRepository<Club, Long> {}