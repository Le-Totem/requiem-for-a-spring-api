package fr.afpa.requiem_for_a_spring.services;

import fr.afpa.requiem_for_a_spring.entities.Instrument;
import fr.afpa.requiem_for_a_spring.entities.Media;
import fr.afpa.requiem_for_a_spring.entities.MediaInstrument;
import fr.afpa.requiem_for_a_spring.repositories.InstrumentRepository;
import fr.afpa.requiem_for_a_spring.repositories.MediaRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

@Service
public class MediaInstrumentService {

    private final MediaRepository mediaRepository;
    private final InstrumentRepository instrumentRepository;

    public MediaInstrumentService(MediaRepository mediaRepository, InstrumentRepository instrumentRepository) {
        this.mediaRepository = mediaRepository;
        this.instrumentRepository = instrumentRepository;
    }

    // Ajouter un instrument à un média
    @Transactional
    public void addInstrumentToMedia(Integer mediaId, Integer instrumentId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media non trouvé : id=" + mediaId));
        Instrument instrument = instrumentRepository.findById(instrumentId)
                .orElseThrow(() -> new RuntimeException("Instrument non trouvé : id=" + instrumentId));

        // Vérifier si le lien existe déjà
        boolean exists = media.getMediaInstruments().stream()
                .anyMatch(mi -> mi.getInstrument().getId().equals(instrumentId));
        if (!exists) {
            MediaInstrument mi = new MediaInstrument(media, instrument);
            media.getMediaInstruments().add(mi);
            mediaRepository.save(media);
        }
    }

    // Supprimer un instrument d’un média
    @Transactional
    public void removeInstrumentFromMedia(Integer mediaId, Integer instrumentId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media non trouvé : id=" + mediaId));

        media.getMediaInstruments().removeIf(mi -> mi.getInstrument().getId().equals(instrumentId));

        mediaRepository.save(media);
    }

    // Modifier un instrument lié à un média (remplacer un instrument par un autre)
    @Transactional
    public void updateInstrumentOfMedia(Integer mediaId, Integer oldInstrumentId, Integer newInstrumentId) {
        Media media = mediaRepository.findById(mediaId)
                .orElseThrow(() -> new RuntimeException("Media non trouvé : id=" + mediaId));
        Instrument newInstrument = instrumentRepository.findById(newInstrumentId)
                .orElseThrow(() -> new RuntimeException("Instrument non trouvé : id=" + newInstrumentId));

        // Supprimer l’ancien instrument
        media.getMediaInstruments().removeIf(mi -> mi.getInstrument().getId().equals(oldInstrumentId));

        // Ajouter le nouveau
        MediaInstrument mi = new MediaInstrument(media, newInstrument);
        media.getMediaInstruments().add(mi);

        mediaRepository.save(media);
    }
}
