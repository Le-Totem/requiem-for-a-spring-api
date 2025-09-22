package fr.afpa.requiem_for_a_spring.web.controllers;

import fr.afpa.requiem_for_a_spring.dtos.MediaDto;
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

    // Récupérer tous les médias
    @GetMapping
    public ResponseEntity<List<MediaDto>> getAllMedia() {
        List<MediaDto> mediaList = mediaService.getAll();
        return ResponseEntity.ok(mediaList);
    }

    // Récupérer un média par ID
    @GetMapping("/{id}")
    public ResponseEntity<MediaDto> getMediaById(@PathVariable Integer id) {
        MediaDto dto = mediaService.getById(id);
        if (dto == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(dto);
    }

    // Créer un média avec instruments
    @PostMapping
    public ResponseEntity<MediaDto> createMedia(@RequestBody MediaDto dto) {
        try {
            MediaDto created = mediaService.createMedia(dto);
            return ResponseEntity.ok(created);
        } catch (RuntimeException ex) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Supprimer un média
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMedia(@PathVariable Integer id) {
        try {
            mediaService.deleteById(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException ex) {
            return ResponseEntity.notFound().build();
        }
    }
}
