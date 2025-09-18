package fr.afpa.requiem_for_a_spring.services;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;

@Service
public class MusicPieceService {

    private MusicPieceRepository musicPieceRepository;

    public MusicPieceService(MusicPieceRepository musicPieceRepository) {
        this.musicPieceRepository = musicPieceRepository;
    }

    /**
     * Récupère toutes les fiches morceaux
     * 
     * @return Une liste de fiches morceaux
     */
    public List<MusicPieceDto> getAll() {
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
        return musicPieceRepository.findAllByIdGroup(id_group).stream().map(musicPiece -> new MusicPieceDto(musicPiece))
                .collect(Collectors.toList());
    }
}
