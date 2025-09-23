package fr.afpa.requiem_for_a_spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.Genre;

@Repository
public interface GenreRepository extends JpaRepository<Genre, Integer> {

    // Récupérer les fiches morceaux d'un ensemble
    List<Genre> findAllByIdMusicPiece_Id(Integer musicPieceId);

}
