package fr.afpa.requiem_for_a_spring.repositories;

import fr.afpa.requiem_for_a_spring.entities.Group;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GroupRepository extends JpaRepository<Group, Integer> {
}
