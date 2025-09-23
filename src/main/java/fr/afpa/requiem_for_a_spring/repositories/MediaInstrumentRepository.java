package fr.afpa.requiem_for_a_spring.repositories;

import fr.afpa.requiem_for_a_spring.entities.MediaInstrument;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaInstrumentRepository extends JpaRepository<MediaInstrument, Integer> {
}
