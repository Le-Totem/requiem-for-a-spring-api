package fr.afpa.requiem_for_a_spring.web.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import fr.afpa.requiem_for_a_spring.services.MusicPieceService;

@RestController
@RequestMapping("/api/tracks")
public class MusicPieceController {

    private final MusicPieceService musicPieceService;

    // constructeur
    public MusicPieceController(MusicPieceService musicPieceService) {
        this.musicPieceService = musicPieceService;
    }

    /**
     * Récupère toutes les fiches morceaux
     * 
     * @return Une liste de fiches morceaux
     */
    @GetMapping
    public ResponseEntity<List<MusicPieceDto>> getAllMusicPieces() {
        return new ResponseEntity<>(musicPieceService.getAll(), HttpStatus.OK);
    }

}
