package fr.afpa.requiem_for_a_spring.repositories;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.User;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Récupérer les fiches morceaux d'un ensemble
    List<User> findAllByUserGroups_Group_Id(Integer id_group);

    Optional<User> findByEmail(String email);

}
