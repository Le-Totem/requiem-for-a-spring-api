package fr.afpa.requiem_for_a_spring.repositories;

import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.MusicPiece;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface MusicPieceRepository extends JpaRepository<MusicPiece, Integer> {

    // Récupérer les fiches morceaux d'un ensemble
     List<MusicPiece> findAllByIdGroup(Integer groupId);

}
