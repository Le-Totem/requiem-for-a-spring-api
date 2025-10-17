package fr.afpa.requiem_for_a_spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {

    // Récupérer toutes les invitations pour un groupe donné
    List<Invitation> findByGroup_Id(Integer groupId);

    List<Invitation> findByEmail(String email);

}
