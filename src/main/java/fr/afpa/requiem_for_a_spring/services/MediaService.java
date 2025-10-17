package fr.afpa.requiem_for_a_spring.services;

import fr.afpa.requiem_for_a_spring.dtos.MediaDto;
import fr.afpa.requiem_for_a_spring.entities.*;
import fr.afpa.requiem_for_a_spring.repositories.InstrumentRepository;
import fr.afpa.requiem_for_a_spring.repositories.MediaRepository;
import fr.afpa.requiem_for_a_spring.repositories.MusicPieceRepository;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import jakarta.transaction.Transactional;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
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

    // dossier où sont stockés les fichiers
    private static final String UPLOAD_DIR = "uploads/";

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

    // ----------------- documents --------------------------------- //

    // récupérer le fichier d'un média
    public ResponseEntity<byte[]> printFile(Integer idMedia) {
        Media media = mediaRepository.findById(idMedia)
                .orElseThrow(() -> new RuntimeException("Média non trouvé : " + idMedia));

        try {
            if (media.getUrl() == null || media.getUrl().isEmpty()) {
                throw new RuntimeException("Ce média n'a pas d'url.");
            }
            Path filePath = Paths.get(UPLOAD_DIR + media.getUrl());
            MediaType mediaTypeSelected = getMediaType(media.getType(), filePath);
            byte[] files = Files.readAllBytes(filePath);
            return ResponseEntity.ok()
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "inline; filename=\"" + filePath.getFileName().toString() + "\"")
                    .contentType(mediaTypeSelected)
                    .body(files);
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la lecture du fichier.");
        }
    }

    // récupère le bon content-type
    private MediaType getMediaType(fr.afpa.requiem_for_a_spring.enums.MediaType type, Path pathFile)
            throws IOException {
        switch (type) {
            case PDF:
                return MediaType.APPLICATION_PDF;
            case IMAGE:
                String detectedType = Files.probeContentType(pathFile);
                return detectedType != null ? MediaType.parseMediaType(detectedType) : MediaType.IMAGE_PNG;
            case VIDEO:
                return MediaType.valueOf("video/mp4");
            case MUSESCORE:
                return MediaType.valueOf("application/vnd.musescore");
            case TUXGUITAR:
                return MediaType.valueOf("application/x-tuxguitar");
            default:
                return MediaType.APPLICATION_OCTET_STREAM;
        }
    }

    // enregistre le fichier dans /uploads
    public String saveFile(MultipartFile file) {
        try {
            if (file.isEmpty()) {
                throw new RuntimeException("Pas de fichier.");
            }

            Path uploadsDir = Paths.get("uploads");
            if (!Files.exists(uploadsDir)) {
                Files.createDirectories(uploadsDir);
            }

            String filename = file.getOriginalFilename(); //
            Path path = uploadsDir.resolve(filename);
            Files.copy(file.getInputStream(), path);
            return filename;
        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de la sauvegarde du fichier.", e);
        }
    }
}
