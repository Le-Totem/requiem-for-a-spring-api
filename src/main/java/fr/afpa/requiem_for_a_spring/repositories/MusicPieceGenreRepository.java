package fr.afpa.requiem_for_a_spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.MusicPieceGenre;
import fr.afpa.requiem_for_a_spring.entities.MusicPieceGenreId;

@Repository
public interface MusicPieceGenreRepository extends JpaRepository<MusicPieceGenre, MusicPieceGenreId> {

}
