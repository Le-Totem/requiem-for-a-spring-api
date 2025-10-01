package fr.afpa.requiem_for_a_spring.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.Invitation;

@Repository
public interface InvitationRepository extends JpaRepository<Invitation, Integer> {
    
    // Récupérer tous les invitations d'un ensemble
    List<Invitation> findById_Group(Integer id_group);
}
