package fr.afpa.requiem_for_a_spring.repositories;

import fr.afpa.requiem_for_a_spring.entities.Instrument;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Integer> {
}
