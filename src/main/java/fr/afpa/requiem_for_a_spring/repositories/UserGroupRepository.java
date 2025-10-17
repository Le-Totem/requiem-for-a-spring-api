package fr.afpa.requiem_for_a_spring.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import fr.afpa.requiem_for_a_spring.entities.UserGroup;
import fr.afpa.requiem_for_a_spring.entities.UserGroupId;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
    @Query("SELECT ug FROM UserGroup ug JOIN FETCH ug.group WHERE ug.user.id = :userId")
    List<UserGroup> findByUser_Id(@Param("userId") UUID userId);

    List<UserGroup> findByGroup_Id(Integer groupId);

    Optional<UserGroup> findByUser_IdAndGroup_Id(UUID userId, Integer groupId);
}
