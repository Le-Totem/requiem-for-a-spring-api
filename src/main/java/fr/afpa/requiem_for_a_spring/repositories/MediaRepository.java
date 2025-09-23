package fr.afpa.requiem_for_a_spring.repositories;

import fr.afpa.requiem_for_a_spring.entities.Media;
import fr.afpa.requiem_for_a_spring.enums.MediaType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {

    // les médias d’un morceau (track)
    List<Media> findByIdTrack_Id(Integer trackId);

    // tous les médias d’un utilisateur
    List<Media> findByIdUser_Id(UUID idUser_id);

    // retrouver par type (PDF, image, etc.)
    List<Media> findByType(MediaType type);
}
