package fr.afpa.requiem_for_a_spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    // Récupérer les fiches morceaux d'un ensemble
<<<<<<< HEAD
    List<Genre> findAllByIdMusicPiece_Id(Integer musicPieceId);
=======
    List<Genre> findAllByMusicPiece_Id(Integer id);
>>>>>>> d32daf3364368aad7c69f8bb59b29b29c7f815dc

}
