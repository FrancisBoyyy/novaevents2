package pt.unl.fct.iadi.novaevents.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.FetchType
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import jakarta.persistence.OneToMany
import pt.unl.fct.iadi.novaevents.domain.enums.ClubCategory

@Entity
class Club(
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long? = null,

    @Column(unique = true)
    var name: String? = null,

    @Column(length = 2000)
    var description: String? = null,

    @Column(
        columnDefinition = "ENUM('ACADEMIC','TECHNOLOGY','ARTS','SPORTS','CULTURAL')"
    )
    @Enumerated(EnumType.STRING)
    var category: ClubCategory? = null,

    @OneToMany(mappedBy = "club", fetch = FetchType.EAGER)
    var events: List<Event> = mutableListOf()
)