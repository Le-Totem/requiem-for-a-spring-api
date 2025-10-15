package fr.afpa.requiem_for_a_spring.services;

import fr.afpa.requiem_for_a_spring.dtos.MediaDto;
import fr.afpa.requiem_for_a_spring.entities.*;
import fr.afpa.requiem_for_a_spring.repositories.InstrumentRepository;
import fr.afpa.requiem_for_a_spring.repositories.MediaRepository;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class MediaService {

    private final MediaRepository mediaRepository;
    private final InstrumentRepository instrumentRepository;
    private final MusicPieceRepository musicPieceRepository;
    private final UserRepository userRepository;

    public MediaService(MediaRepository mediaRepository,
            InstrumentRepository instrumentRepository,
            MusicPieceRepository musicPieceRepository,
            UserRepository userRepository) {
        this.mediaRepository = mediaRepository;
        this.instrumentRepository = instrumentRepository;
        this.musicPieceRepository = musicPieceRepository;
        this.userRepository = userRepository;
    }

    // Récupérer tous les médias
    public List<MediaDto> getAll() {
        return mediaRepository.findAll().stream()
                .map(MediaDto::new)
                .collect(Collectors.toList());
    }

    // Récupérer un média par ID
    public MediaDto getById(Integer id) {
        return mediaRepository.findById(id)
                .map(MediaDto::new)
                .orElse(null);
    }

    // Récupérer tous les médias d'une fiche morceau
    public List<MediaDto> getMediasByIdMusicPiece(Integer id) {
        return mediaRepository.findByIdTrack_Id(id).stream().map(MediaDto::new).collect(Collectors.toList());
    }

    // Créer un média et associer instruments, track et user
    @Transactional
    public MediaDto createMedia(Integer idTrack, MediaDto dto, User user) {
        Media media = new Media();
        media.setType(dto.getType());
        media.setTitle(dto.getTitle());
        media.setUrl(dto.getUrl());
        media.setDateAdded(LocalDate.now());
        media.setDateModified(dto.getDateModified());

        // Lier la piste
        MusicPiece track = musicPieceRepository.findById(idTrack)
                .orElseThrow(() -> new RuntimeException("Track non trouvé : id=" + idTrack));
        media.setIdTrack(track);

        // Lier l’utilisateur
        media.setIdUser(user);

        // Sauvegarder le média pour générer l’ID
        Media savedMedia = mediaRepository.save(media);

        // Associer les instruments
        if (dto.getInstrumentIds() != null) {
            for (Integer instId : dto.getInstrumentIds()) {
                Instrument instrument = instrumentRepository.findById(instId)
                        .orElseThrow(() -> new RuntimeException("Instrument non trouvé : id=" + instId));

                MediaInstrument mi = new MediaInstrument(savedMedia, instrument);
                savedMedia.getMediaInstruments().add(mi);

            }
            savedMedia = mediaRepository.save(savedMedia);
        }

        return new MediaDto(savedMedia);
    }

    @Transactional
    public MediaDto updateMedia(Integer id, MediaDto dto, User user) {
        // Vérifier que le média existe
        Media existingMedia = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media non trouvé : id=" + id));

        // Mise à jour des champs simples
        if (dto.getTitle() != null)
            existingMedia.setTitle(dto.getTitle());
        if (dto.getUrl() != null)
            existingMedia.setUrl(dto.getUrl());
        if (dto.getType() != null)
            existingMedia.setType(dto.getType());
        existingMedia.setDateModified(LocalDate.now());

        // Mise à jour du track (si fourni)
        if (dto.getTrackId() != null) {
            MusicPiece track = musicPieceRepository.findById(dto.getTrackId())
                    .orElseThrow(() -> new RuntimeException("Track non trouvé : id=" + dto.getTrackId()));
            existingMedia.setIdTrack(track);
        }

        // Mise à jour des instruments associés
        if (dto.getInstrumentIds() != null) {
            // Supprimer les anciens liens
            existingMedia.getMediaInstruments().clear();

            // Ajouter les nouveaux
            for (Integer instId : dto.getInstrumentIds()) {
                Instrument instrument = instrumentRepository.findById(instId)
                        .orElseThrow(() -> new RuntimeException("Instrument non trouvé : id=" + instId));

                MediaInstrument mi = new MediaInstrument(existingMedia, instrument);
                existingMedia.getMediaInstruments().add(mi);
            }
        }

        // Sauvegarde
        Media updatedMedia = mediaRepository.save(existingMedia);

        return new MediaDto(updatedMedia);
    }

    // Supprimer un média
    @Transactional
    public void deleteById(Integer id) {
        Media media = mediaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Media non trouvé : id=" + id));
        mediaRepository.delete(media);
    }
}
