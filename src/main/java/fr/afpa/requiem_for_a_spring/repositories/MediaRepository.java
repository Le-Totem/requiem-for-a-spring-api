package fr.afpa.requiem_for_a_spring.repositories;

import fr.afpa.requiem_for_a_spring.entities.Media;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MediaRepository extends JpaRepository<Media, Integer> {

    // les médias d’un morceau (track)
    List<Media> findByIdTrack_Id(Integer trackId);

    // tous les médias d’un utilisateur
    List<Media> findByIdUser_Id(Integer userId);

    //retrouver par type (PDF, image, etc.)
    List<Media> findByType(Enum type);
}
