package fr.afpa.requiem_for_a_spring.mappers;

import fr.afpa.requiem_for_a_spring.dtos.InstrumentDto;
import fr.afpa.requiem_for_a_spring.entities.Instrument;
import org.springframework.stereotype.Service;

@Service
public class InstrumentMapper {
    // ENTITY TO DTO
    public InstrumentDto convertToDto(Instrument instrument) {
        return new InstrumentDto(instrument);
    }

    // DTO TO ENTITY
    public Instrument convertToEntity(InstrumentDto instrumentDto) {
        return new Instrument(instrumentDto);
    }
}
