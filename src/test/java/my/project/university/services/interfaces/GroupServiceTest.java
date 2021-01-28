package my.project.university.services.interfaces;

import my.project.university.mappers.StudentMapper;
import my.project.university.repository.GroupRepository;
import my.project.university.repository.StudentRepository;
import my.project.university.repository.TrainingProgramRepository;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.GroupMapper;
import my.project.university.models.Group;
import my.project.university.models.TrainingProgram;
import my.project.university.models.dto.GroupDto;
import my.project.university.services.GroupServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class GroupServiceTest {
    private GroupRepository groupRepository = mock(GroupRepository.class);
    private GroupMapper groupMapper = mock(GroupMapper.class);
    private TrainingProgramRepository trainingProgramRepository = mock(TrainingProgramRepository.class);
    private StudentRepository studentRepository = mock(StudentRepository.class);
    private StudentMapper studentMapper = mock(StudentMapper.class);

    private GroupService groupService = new GroupServiceImpl(groupRepository, trainingProgramRepository,
            studentRepository, studentMapper, groupMapper);

    @Test
    void findByIdShouldThrowNotFoundEntityExceptionWhenEntityNotFound() {
        when(groupRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> groupService.findById(4));
    }

    @Test
    void findByDescriptionShouldThrowNotFoundEntityExceptionWhenEntityNotFound() {
        when(groupRepository.findByDescription("unknown")).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> groupService.findByDescription("unknown"));
    }


    @Test
    void findById() {
        Group group = new Group();

        when(groupRepository.findById(1)).thenReturn(Optional.of(group));

        groupService.findById(1);

        InOrder inOrder = inOrder(groupRepository, groupMapper);
        inOrder.verify(groupRepository).findById(1);
        inOrder.verify(groupMapper).toDto(group);

        verifyNoMoreInteractions(groupRepository, groupMapper);

    }

    @Test
    void findByDescription() {
        Group group = new Group();

        when(groupRepository.findByDescription("group")).thenReturn(Optional.of(group));

        groupService.findByDescription("group");

        InOrder inOrder = inOrder(groupRepository, groupMapper);
        inOrder.verify(groupRepository).findByDescription("group");
        inOrder.verify(groupMapper).toDto(group);

        verifyNoMoreInteractions(groupRepository, groupMapper);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Group> groups = new PageImpl<>(List.of(new Group()));

        when(groupRepository.findAll(pageable)).thenReturn(groups);

        groupService.findAll(pageable);

        InOrder inOrder = inOrder(groupRepository, groupMapper);
        inOrder.verify(groupRepository).findAll(pageable);
        inOrder.verify(groupMapper, times(groups.getSize())).toDto(any());

        verifyNoMoreInteractions(groupRepository, groupMapper);
    }

    @Test
    void add() {
        Group adding = mock(Group.class);
        Group added = new Group();
        GroupDto groupDto = new GroupDto();
        TrainingProgram trainingProgram = new TrainingProgram();

        when(groupMapper.fromDto(groupDto)).thenReturn(adding);
        when(trainingProgramRepository.findBySpeciality(groupDto.getTrainingProgram()))
                .thenReturn(Optional.of(trainingProgram));
        when(groupRepository.save(adding)).thenReturn(added);

        groupService.saveOrUpdate(groupDto);

        InOrder inOrder = inOrder(groupRepository, groupMapper, trainingProgramRepository, adding);
        inOrder.verify(groupMapper).fromDto(groupDto);
        inOrder.verify(trainingProgramRepository).findBySpeciality(groupDto.getTrainingProgram());
        inOrder.verify(adding).setTrainingProgram(trainingProgram);
        inOrder.verify(groupRepository).save(adding);
        inOrder.verify(groupMapper).toDto(added);

        verifyNoMoreInteractions(groupRepository, groupMapper, trainingProgramRepository, adding);
    }

    @Test
    void update() {
        Group updating = mock(Group.class);
        Group updated = new Group();
        GroupDto groupDto = new GroupDto();
        TrainingProgram trainingProgram = new TrainingProgram();

        when(groupMapper.fromDto(groupDto)).thenReturn(updating);
        when(trainingProgramRepository.findBySpeciality(groupDto.getTrainingProgram()))
                .thenReturn(Optional.of(trainingProgram));
        when(groupRepository.save(updating)).thenReturn(updated);

        groupService.saveOrUpdate(groupDto);

        InOrder inOrder = inOrder(groupRepository, groupMapper, trainingProgramRepository, updating);
        inOrder.verify(groupMapper).fromDto(groupDto);
        inOrder.verify(trainingProgramRepository).findBySpeciality(groupDto.getTrainingProgram());
        inOrder.verify(updating).setTrainingProgram(trainingProgram);
        inOrder.verify(groupRepository).save(updating);
        inOrder.verify(groupMapper).toDto(updated);

        verifyNoMoreInteractions(groupRepository, groupMapper, trainingProgramRepository, updating);
    }

    @Test
    void delete() {
        groupService.delete(1);
        verify(groupRepository).deleteById(1);

        verifyNoMoreInteractions(groupRepository);
    }

    @Test
    void deleteShouldThrowNotFoundEntityExceptionWhenDeletingEntityWhichNotExist() {
        doThrow(NotFoundEntityException.class).when(groupRepository).deleteById(4);
        assertThrows(NotFoundEntityException.class, () -> groupService.delete(4));
    }
}