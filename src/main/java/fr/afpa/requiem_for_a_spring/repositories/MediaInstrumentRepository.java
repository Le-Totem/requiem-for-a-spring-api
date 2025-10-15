package fr.afpa.requiem_for_a_spring.repositories;

import fr.afpa.requiem_for_a_spring.entities.MediaInstrument;
import fr.afpa.requiem_for_a_spring.entities.MediaInstrumentId;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MediaInstrumentRepository extends JpaRepository<MediaInstrument, MediaInstrumentId> {

    // Récupérer les instruments d'un média
    List<MediaInstrument> findAllByMedia_Id(Integer id_media);
}
