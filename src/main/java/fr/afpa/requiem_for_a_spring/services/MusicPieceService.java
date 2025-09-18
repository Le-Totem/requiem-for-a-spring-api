package fr.afpa.requiem_for_a_spring.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import fr.afpa.requiem_for_a_spring.entities.MusicPiece;
import fr.afpa.requiem_for_a_spring.mappers.MusicPieceMapper;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@Service
public class MusicPieceService {

    private MusicPieceRepository musicPieceRepository;

    private MusicPieceMapper musicPieceMapper;

    public MusicPieceService(MusicPieceRepository musicPieceRepository, MusicPieceMapper musicPieceMapper) {
        this.musicPieceRepository = musicPieceRepository;
        this.musicPieceMapper = musicPieceMapper;
    }

    /**
     * Récupère toutes les fiches morceaux
     * 
     * @return Une liste de fiches morceaux
     */
    public List<MusicPieceDto> getAllMusicPieces() {
        return musicPieceRepository.findAll().stream().map(musicPiece -> new MusicPieceDto(musicPiece))
                .collect(Collectors.toList());
    }

    /**
     * Récupère les fiches morceaux d'un ensemble
     * 
     * @param id_group L'id de l'ensemble
     * @return Une liste de fiches morceaux
     */
    public List<MusicPieceDto> getAllByIdGroup(Integer id_group) {
        return MusicPieceRepository.findAllByIdGroup(id_group).stream().map(musicPiece -> new MusicPieceDto(musicPiece))
                .collect(Collectors.toList());
    }

    /**
     * Récupère une fiche morceaux en fonction de son id
     * 
     * @param id L'id de la fiche morceaux
     * @return Une fiche morceaux
     */
    public MusicPieceDto getOneMusicPiece(Integer id) {
        return musicPieceMapper.convertToDto(musicPieceRepository.findById(id).orElse(null));
    }

    /**
     * Crée une fiche morceau
     * 
     * @param musicPieceDto
     * @return
     */
    public MusicPieceDto createMusicPiece(MusicPieceDto musicPieceDto) {
        MusicPiece musicPiece = musicPieceMapper.convertToEntity(musicPieceDto);
        musicPiece = musicPieceRepository.save(musicPiece);
        musicPieceDto.setId(musicPiece.getId());
        return musicPieceDto;
    }

    /**
     * Modifie une fiche morceau
     * 
     * @param id            L'id de la fiche morceau à modifier
     * @param musicPieceDto
     * @return
     */
    public MusicPieceDto updateMusicPiece(Integer id, MusicPieceDto musicPieceDto) {
        Optional<MusicPiece> originalMusic = musicPieceRepository.findById(id);

        // Retourne une erreur si la fiche morceau n'existe pas
        if (originalMusic.isEmpty()) {
            throw new EntityNotFoundException("La fiche morceau est introuvable.");
        }

        // Retourne une erreur si l'id ne correspond à aucun id en BDD
        if (!id.equals(musicPieceDto.getId())) {
            throw new IllegalArgumentException("L'id ne correspond à aucune fiche morceau.");
        }

        MusicPiece musicPiece = originalMusic.get();
        return musicPieceMapper.convertToDto(musicPieceRepository.save(musicPiece));
    }

    /**
     * Supprime une fiche morceau
     * 
     * @param id       L'id de la fiche morceau à supprimer
     * @param response Réponse HTTP renvoyée
     */
    public void removeMusicPiece(Integer id, HttpServletResponse response) {
        if (musicPieceRepository.existsById(id)) {
            musicPieceRepository.deleteById(id);
            // La requête est réussie et aucune nouvelle information à retourner
            response.setStatus(HttpServletResponse.SC_NO_CONTENT);
        } else {
            // La requête a échoué et renvoie une erreur 404
            response.setStatus(HttpServletResponse.SC_NOT_FOUND);
        }
    }
}
