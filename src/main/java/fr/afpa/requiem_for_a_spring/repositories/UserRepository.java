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

    /**
     * Recherche un utilisateur à partir de son adresse e-mail.
     *
     * @param email Adresse e-mail de l'utilisateur.
     * @return Un {@link Optional} contenant l'utilisateur s'il existe, sinon vide.
     */
    Optional<User> findByEmail(String email);

    /**
     * Recherche un utilisateur à partir de son jeton de réinitialisation de mot de
     * passe.
     *
     * @param token Jeton de réinitialisation associé à l'utilisateur.
     * @return Un {@link Optional} contenant l'utilisateur correspondant, sinon
     *         vide.
     */
    Optional<User> findByResetToken(String token);

}
