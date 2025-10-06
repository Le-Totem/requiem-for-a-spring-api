package fr.afpa.requiem_for_a_spring.web.controllers;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import fr.afpa.requiem_for_a_spring.config.jwt.RequireRole;
import fr.afpa.requiem_for_a_spring.dtos.GenreDto;
import fr.afpa.requiem_for_a_spring.dtos.MediaDto;
import fr.afpa.requiem_for_a_spring.dtos.MusicPieceDto;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.services.GenreService;
import fr.afpa.requiem_for_a_spring.services.MediaService;
import fr.afpa.requiem_for_a_spring.services.MusicPieceService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.servlet.http.HttpServletResponse;

@RestController
@RequestMapping("/api/tracks")
public class MusicPieceController {

    private final MusicPieceService musicPieceService;
    private final GenreService genreService;
    private final MediaService mediaService;

    // constructeur
    public MusicPieceController(MusicPieceService musicPieceService, GenreService genreService,
            MediaService mediaService) {
        this.musicPieceService = musicPieceService;
        this.genreService = genreService;
        this.mediaService = mediaService;
    }

    /**
     * Requête pour récupérer toutes les fiches morceaux ✅
     * 
     * @return Une liste de fiches morceaux
     */
    @GetMapping
    public ResponseEntity<List<MusicPieceDto>> getAllMusicPieces() {
        return new ResponseEntity<>(musicPieceService.getAllMusicPieces(), HttpStatus.OK);
    }

    /**
     * Requête pour récupérer une fiche morceau en fonction de son id ✅
     * 
     * @param id L'id de la fiche morceau
     * @return Une fiche morceau
     */
    @GetMapping("/{id}")
    public ResponseEntity<MusicPieceDto> getOneMusicPiece(@PathVariable Integer id) {
        return new ResponseEntity<>(musicPieceService.getOneMusicPiece(id), HttpStatus.OK);
    }

    /**
     * Requête pour récupérer toutes les fiches morceaux d'un ensemble ✅
     * 
     * @return Une liste de fiches morceaux
     */
    @GetMapping("/group/{id}")
    public ResponseEntity<List<MusicPieceDto>> getAllMusicPiecesByIdGroup(@PathVariable Integer id) {
        return new ResponseEntity<>(musicPieceService.getAllByIdGroup(id), HttpStatus.OK);
    }

    /**
     * Requête pour récupérer tous les genres d'une fiche morceau ✅
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}/all-genres")
    public ResponseEntity<List<GenreDto>> getAllGenres(@PathVariable Integer id) {
        return new ResponseEntity<>(genreService.getAllGenresByIdMusicPiece(id), HttpStatus.OK);
    }

    /**
     * Requête pour récupérer tous les médias d'une fiche morceau ✅
     * 
     * @param id
     * @return
     */
    @GetMapping("/{id}/medias")
    public ResponseEntity<List<MediaDto>> getAllMedias(@PathVariable Integer id) {
        return new ResponseEntity<>(mediaService.getMediasByIdMusicPiece(id), HttpStatus.OK);
    }

    /**
     * Requête pour créer une fiche morceau ✅
     * 
     * @param musicPieceDto
     * @return
     */
    @PostMapping
    @RequireRole(role = Role.MODERATEUR)
    public ResponseEntity<MusicPieceDto> createMusicPiece(@RequestBody MusicPieceDto musicPieceDto) {
        return new ResponseEntity<>(musicPieceService.createMusicPiece(musicPieceDto), HttpStatus.CREATED);
    }

    /**
     * Requête pour créer un genre ✅
     * 
     * @param genreDto
     * @return
     */
    @PostMapping("/add-genre")
    @RequireRole(role = Role.MODERATEUR)
    public ResponseEntity<GenreDto> createGenre(@RequestBody GenreDto genreDto) {
        return new ResponseEntity<>(genreService.createGenre(genreDto), HttpStatus.CREATED);
    }

    /**
     * Requête pour ajouter un ou plusieurs genres à une fiche morceau ✅
     * 
     * @param entity
     * @return
     */
    @PostMapping("/{id}/add-genre")
    @RequireRole(role = Role.MODERATEUR)
    public ResponseEntity<List<GenreDto>> addGenresToMusicPiece(@PathVariable Integer id,
            @RequestBody List<GenreDto> genres) {
        return new ResponseEntity<>(genreService.addGenres(id, genres), HttpStatus.CREATED);
    }

    /**
     * Requête pour modifier une fiche morceau ✅
     * 
     * @param id            L'id de la fiche morceau à modifier
     * @param musicPieceDto
     * @return
     */
    @PatchMapping("/{id}")
    @RequireRole(role = Role.MODERATEUR)
    public ResponseEntity<MusicPieceDto> updateMusicPiece(@PathVariable Integer id,
            @RequestBody MusicPieceDto musicPieceDto) {
        try {
            // La requête a réussi et la fiche morceau a été modifiée
            return new ResponseEntity<>(musicPieceService.updateMusicPiece(id, musicPieceDto), HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            // La requête a échoué, la fiche morceau n'a pas été trouvée + erreur 404
            return new ResponseEntity<>(null, HttpStatus.NOT_FOUND);
        }
    }

    /**
     * Requête pour supprimer une fiche morceau ✅
     * 
     * @param id       L'id de la fiche morceau à supprimer
     * @param response Réponse HTTP renvoyée
     */
    @DeleteMapping("/{id}")
    @RequireRole(role = Role.ADMIN)
    public void removeMusicPiece(@PathVariable Integer id, HttpServletResponse response) {
        musicPieceService.removeMusicPiece(id, response);
    }
}
