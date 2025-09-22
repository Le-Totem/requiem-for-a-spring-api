package fr.afpa.requiem_for_a_spring.mappers;

import org.springframework.stereotype.Service;

import fr.afpa.requiem_for_a_spring.dtos.UserDto;
import fr.afpa.requiem_for_a_spring.entities.User;

@Service
public class UserMapper {
    // ENTITY TO DTO
    public UserDto convertToDto(User user) {
        return new UserDto(user);
    }

    // DTO TO ENTITY
    public User convertToEntity(UserDto userDto) {
        return new User(userDto);
    }
}
