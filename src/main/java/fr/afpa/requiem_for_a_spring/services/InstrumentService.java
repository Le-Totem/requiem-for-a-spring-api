package fr.afpa.requiem_for_a_spring.services;

import fr.afpa.requiem_for_a_spring.dtos.InstrumentDto;
import fr.afpa.requiem_for_a_spring.entities.Instrument;
import fr.afpa.requiem_for_a_spring.mappers.InstrumentMapper;
import fr.afpa.requiem_for_a_spring.repositories.InstrumentRepository;
import fr.afpa.requiem_for_a_spring.repositories.MediaInstrumentRepository;
import fr.afpa.requiem_for_a_spring.repositories.MediaRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class InstrumentService {

    private final MediaInstrumentRepository mediaInstrumentRepository;

    private final InstrumentRepository instrumentRepository;
    private final InstrumentMapper instrumentMapper;
    private final MediaRepository mediaRepository;

    public InstrumentService(InstrumentRepository instrumentRepository, InstrumentMapper instrumentMapper,
            MediaRepository mediaRepository, MediaInstrumentRepository mediaInstrumentRepository) {
        this.instrumentRepository = instrumentRepository;
        this.instrumentMapper = instrumentMapper;
        this.mediaRepository = mediaRepository;
        this.mediaInstrumentRepository = mediaInstrumentRepository;
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

    // Récupérer la liste des instruments pour 1 média
    public List<InstrumentDto> findAllByIdMedia(Integer id_media) {

        if (!mediaRepository.existsById(id_media)) {
            throw new EntityNotFoundException("Le média est introuvable.");
        }

        return mediaInstrumentRepository.findAllByMedia_Id(id_media)
                .stream()
                .map(mediaInstrument -> new InstrumentDto(mediaInstrument.getInstrument()))
                .collect(Collectors.toList());
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
