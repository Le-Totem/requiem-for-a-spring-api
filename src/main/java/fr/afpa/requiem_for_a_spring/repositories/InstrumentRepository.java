package fr.afpa.requiem_for_a_spring.repositories;

import fr.afpa.requiem_for_a_spring.entities.Instrument;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstrumentRepository extends JpaRepository<Instrument, Integer> {

    // Récupérer les instruments d'un média
    List<Instrument> findAllByMedia_Id(Integer id_media);
}
