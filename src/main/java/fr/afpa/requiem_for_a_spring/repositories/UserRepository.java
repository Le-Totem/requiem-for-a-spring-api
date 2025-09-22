package fr.afpa.requiem_for_a_spring.repositories;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.User;
import fr.afpa.requiem_for_a_spring.entities.UserGroup;

import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface UserRepository extends JpaRepository<User, UUID> {
    // Récupérer les fiches morceaux d'un ensemble
    List<User> findAllUsersByIdGroup(Integer id_group);

    // Récupérer la table de jointure user_group en fonction de l'id
    UserGroup findUserGroupByIds(UUID id_user, Integer id_group);

}
