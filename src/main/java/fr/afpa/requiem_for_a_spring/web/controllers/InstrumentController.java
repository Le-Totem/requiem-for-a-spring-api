package fr.afpa.requiem_for_a_spring.web.controllers;

import fr.afpa.requiem_for_a_spring.dtos.InstrumentDto;
import fr.afpa.requiem_for_a_spring.enums.Role;
import fr.afpa.requiem_for_a_spring.services.InstrumentService;
import fr.afpa.requiem_for_a_spring.config.jwt.RequireRole;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/instruments")
public class InstrumentController {

    private final InstrumentService instrumentService;

    public InstrumentController(InstrumentService instrumentService) {
        this.instrumentService = instrumentService;
    }

    // Récupérer tous les instruments
    @GetMapping
    public ResponseEntity<List<InstrumentDto>> getAllInstruments() {
        List<InstrumentDto> instruments = instrumentService.findAll();
        return ResponseEntity.ok(instruments);
    }

    // Récupérer un instrument par ID
    @GetMapping("/{id}")
    public ResponseEntity<InstrumentDto> getInstrumentById(@PathVariable Integer id) {
        try {
            InstrumentDto dto = instrumentService.findById(id);
            return ResponseEntity.ok(dto);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Créer un nouvel instrument
    @PostMapping
    @RequireRole(role = Role.MODERATEUR)
    public ResponseEntity<InstrumentDto> createInstrument(@RequestBody InstrumentDto dto) {
        try {
            InstrumentDto created = instrumentService.create(dto);
            return ResponseEntity.ok(created);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Mettre à jour un instrument
    @PutMapping("/{id}")
    @RequireRole(role = Role.MODERATEUR)
    public ResponseEntity<InstrumentDto> updateInstrument(@PathVariable Integer id,
                                                          @RequestBody InstrumentDto dto) {
        try {
            InstrumentDto updated = instrumentService.update(id, dto);
            return ResponseEntity.ok(updated);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    // Supprimer un instrument
    @DeleteMapping("/{id}")
    @RequireRole(role = Role.ADMIN)
    public ResponseEntity<Void> deleteInstrument(@PathVariable Integer id) {
        try {
            instrumentService.delete(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}
