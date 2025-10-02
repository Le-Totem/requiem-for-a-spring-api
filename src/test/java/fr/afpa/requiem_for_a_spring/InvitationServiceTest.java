package fr.afpa.requiem_for_a_spring;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.contains;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Arrays;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import fr.afpa.requiem_for_a_spring.dtos.InvitationDto;
import fr.afpa.requiem_for_a_spring.entities.Group;
import fr.afpa.requiem_for_a_spring.entities.Invitation;
import fr.afpa.requiem_for_a_spring.enums.Status;
import fr.afpa.requiem_for_a_spring.mailer.EmailService;
import fr.afpa.requiem_for_a_spring.mappers.InvitationMapper;
import fr.afpa.requiem_for_a_spring.repositories.GroupRepository;
import fr.afpa.requiem_for_a_spring.repositories.InvitationRepository;
import fr.afpa.requiem_for_a_spring.repositories.UserRepository;
import fr.afpa.requiem_for_a_spring.services.InvitationService;

@ExtendWith(MockitoExtension.class)
public class InvitationServiceTest {

    @InjectMocks
    private InvitationService invitationService;

    @InjectMocks
    private EmailService emailService;

    @Mock
    private InvitationRepository invitationRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private GroupRepository groupRepository;

    @Mock
    private InvitationMapper invitationMapper;

    @Test
    public void testFindAll() {
        Group group = new Group();
        group.setId(1);
        group.setName("Chorale");

        Invitation invitation1 = new Invitation();
        invitation1.setId(1);
        invitation1.setEmail("nina@mail.com");
        invitation1.setGroup(group);

        Invitation invitation2 = new Invitation();
        invitation2.setId(2);
        invitation2.setEmail("jean@mail.com");
        invitation2.setGroup(group);

        List<Invitation> invitations = Arrays.asList(invitation1, invitation2);

        Mockito.when(invitationRepository.findByGroup_Id(group.getId()))
                .thenReturn(invitations);

        List<InvitationDto> result = invitationService.getAllInvitations(group.getId());

        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("nina@mail.com", result.get(0).getEmail());
        assertEquals("jean@mail.com", result.get(1).getEmail());
    }

    @Test
    public void testCreateInvitation() {
        Group group = new Group();
        group.setId(1);
        group.setName("Chorale");

        InvitationDto invitationDto = new InvitationDto();
        invitationDto.setEmail("jean@mail.com");
        invitationDto.setGroup(group);

        when(groupRepository.findById(group.getId())).thenReturn(Optional.of(group));
        when(userRepository.findByEmail(invitationDto.getEmail())).thenReturn(Optional.empty());
        when(invitationRepository.save(any(Invitation.class))).thenAnswer(invocation -> invocation.getArgument(0));

        InvitationDto result = invitationService.createInvitation(invitationDto);

        assertNotNull(result);
        assertEquals(invitationDto.getEmail(), result.getEmail());
        assertEquals(Status.PENDING, result.getStatus());
        assertEquals(group.getId(), result.getGroup().getId());

        verify(emailService).sendSimpleMessage(
                eq("invite@mail.com"),
                contains("Invitation à rejoindre la plateforme"),
                anyString());
        verify(invitationRepository).save(any(Invitation.class));
    }
}
