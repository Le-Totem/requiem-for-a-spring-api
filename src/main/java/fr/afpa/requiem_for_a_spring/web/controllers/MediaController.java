package fr.afpa.requiem_for_a_spring.web.controllers;

import fr.afpa.requiem_for_a_spring.config.jwt.RequireRole;
import fr.afpa.requiem_for_a_spring.dtos.MediaDto;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.services.MediaService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/media")
public class MediaController {

    private final MediaService mediaService;

    public MediaController(MediaService mediaService) {
        this.mediaService = mediaService;
    }

    /**
     * Requête pour récupérer tous les media existants. ✅
     *
     * @return
     */
    @GetMapping
    @RequireRole(role = Role.UTILISATEUR)
    public ResponseEntity<List<MediaDto>> getAllMedia() {
        List<MediaDto> mediaList = mediaService.getAll();
        return ResponseEntity.ok(mediaList);
    }

    /**
     * Requête pour récupérer un existants en fonction de son id. ✅
     *
     * @return
     */
    @GetMapping("/{id}")
    @RequireRole(role = Role.UTILISATEUR)
    public ResponseEntity<MediaDto> getMediaById(@PathVariable Integer id) {
        MediaDto dto = mediaService.getById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    /**
     * Requête pour créer un media. ✅
     *
     * @return
     */
    @PostMapping
    @RequireRole(role = Role.MODERATEUR)
    public ResponseEntity<MediaDto> createMedia(@RequestBody MediaDto dto) {
        try {
            MediaDto created = mediaService.createMedia(dto);
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    /**
     * Requête pour supprimer un media. ✅
     *
     * @return
     */
    // Supprimer un média
    @DeleteMapping("/{id}")
    @RequireRole(role = Role.ADMIN)
    public ResponseEntity<Void> deleteMedia(@PathVariable Integer id) {
        try {
            mediaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
