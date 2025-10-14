package fr.afpa.requiem_for_a_spring.services;

import fr.afpa.requiem_for_a_spring.dtos.InstrumentDto;
import fr.afpa.requiem_for_a_spring.entities.Instrument;
import fr.afpa.requiem_for_a_spring.mappers.InstrumentMapper;
import fr.afpa.requiem_for_a_spring.repositories.InstrumentRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstrumentService {

    private final InstrumentRepository instrumentRepository;
    private final InstrumentMapper instrumentMapper;

    public InstrumentService(InstrumentRepository instrumentRepository, InstrumentMapper instrumentMapper) {
        this.instrumentRepository = instrumentRepository;
        this.instrumentMapper = instrumentMapper;
    }

    // Récupérer tous les instruments
    public List<InstrumentDto> findAll() {
        return instrumentRepository.findAll().stream()
                .map(instrumentMapper::convertToDto)
                .collect(Collectors.toList());
    }

    // Récupérer un instrument par ID
    public InstrumentDto findById(Integer id) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instrument introuvable avec l'id : " + id));
        return instrumentMapper.convertToDto(instrument);
    }

    // Créer un nouvel instrument
    public InstrumentDto create(InstrumentDto instrumentDto) {
        Instrument instrument = instrumentMapper.convertToEntity(instrumentDto);
        Instrument saved = instrumentRepository.save(instrument);
        return instrumentMapper.convertToDto(saved);
    }

    // Mettre à jour un instrument existant
    public InstrumentDto update(Integer id, InstrumentDto instrumentDto) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instrument introuvable avec l'id : " + id));
        instrument.setName(instrumentDto.getName());

        Instrument updated = instrumentRepository.save(instrument);
        return instrumentMapper.convertToDto(updated);
    }

    // Supprimer un instrument
    public void delete(Integer id) {
        Instrument instrument = instrumentRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Instrument introuvable avec l'id : " + id));
        instrumentRepository.delete(instrument);
    }
}
