package fr.afpa.requiem_for_a_spring.repositories;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.UserGroup;
import fr.afpa.requiem_for_a_spring.entities.UserGroupId;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
    // Récupérer la table de jointure user_group en fonction de l'id
    UserGroup findByUser_IdAndGroup_Id(UUID id_user, Integer id_group);
}
