package fr.afpa.requiem_for_a_spring.repositories;

import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.MusicPiece;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MusicPieceRepository extends JpaRepository<MusicPiece, Integer> {

}
