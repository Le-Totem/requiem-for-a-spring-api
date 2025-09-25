package fr.afpa.requiem_for_a_spring;

import fr.afpa.requiem_for_a_spring.dtos.GroupDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.mappers.GroupMapper;
import fr.afpa.requiem_for_a_spring.repositories.GroupRepository;
import fr.afpa.requiem_for_a_spring.services.GroupService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(MockitoExtension.class)
public class GroupServiceTests {

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private GroupMapper groupMapper;

    @InjectMocks
    private GroupService groupService;

    @Test
    public void testFindAll() {
        Mockito.when(groupRepository.findAll()).thenReturn(Arrays.asList(new Group()));
        List<GroupDto> groups = groupService.findAll();
        assertEquals(1, groups.size());
    }

    @Test
    public void testFindById() {
        // Arrange
        Group group = new Group();
        group.setId(1);
        group.setName("Rock Band");

        GroupDto dto = new GroupDto();
        dto.setId(1);
        dto.setName("Rock Band");

        Mockito.when(groupRepository.findById(Mockito.any()))
                .thenReturn(Optional.of(group));

        // Mocker le mapper
        Mockito.when(groupMapper.convertToDto(group)).thenReturn(dto);

        // Act
        GroupDto result = groupService.findById(1);

        // Assert
        assertEquals("Rock Band", result.getName());
        Mockito.verify(groupRepository).findById(1);
        Mockito.verify(groupMapper).convertToDto(group);

    }

    @Test
    public void testSave() {
        // Arrange
        GroupDto inputDto = new GroupDto();
        inputDto.setName("Rock Band");

        Group entity = new Group();
        entity.setName("Rock Band");

        Group savedEntity = new Group();
        savedEntity.setId(1);
        savedEntity.setName("Rock Band");

        GroupDto outputDto = new GroupDto();
        outputDto.setId(1);
        outputDto.setName("Rock Band");

        // Mock des conversions
        Mockito.when(groupMapper.convertToEntity(inputDto)).thenReturn(entity);
        Mockito.when(groupRepository.save(entity)).thenReturn(savedEntity);
        Mockito.when(groupMapper.convertToDto(savedEntity)).thenReturn(outputDto);

        // Act
        GroupDto result = groupService.save(inputDto);

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getId());
        assertEquals("Rock Band", result.getName());

        // Vérifier que les mocks ont été appelés
        Mockito.verify(groupMapper).convertToEntity(inputDto); // Vérifie que le service renvoie bien un DTO
        Mockito.verify(groupRepository).save(entity); // Vérifie que l’ID est correct
        Mockito.verify(groupMapper).convertToDto(savedEntity); // Vérifie que le nom est correct
    }

    @Test
    public void testDeleteById() {
        // Act
        groupService.deleteById(1);

        // Assert
        Mockito.verify(groupRepository).deleteById(1);
    }

}
