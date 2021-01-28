package my.project.university.services.interfaces;

import my.project.university.mappers.CourseMapper;
import my.project.university.repository.CourseRepository;
import my.project.university.repository.TrainingProgramRepository;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.TrainingProgramMapper;
import my.project.university.models.TrainingProgram;
import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.services.TrainingProgramServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TrainingProgramServiceTest {
    private final TrainingProgramRepository trainingProgramRepository = mock(TrainingProgramRepository.class);
    private final TrainingProgramMapper trainingProgramMapper = mock(TrainingProgramMapper.class);
    private final CourseRepository courseRepository = mock(CourseRepository.class);
    private final CourseMapper courseMapper = mock(CourseMapper.class);

    private TrainingProgramService trainingProgramService = new TrainingProgramServiceImpl(trainingProgramRepository, courseRepository,
            trainingProgramMapper, courseMapper);

    @Test
    void findByIdShouldThrowNotFoundEntityExceptionWhenEntityNotFound() {
        when(trainingProgramRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> trainingProgramService.findById(4));
    }

    @Test
    void findBySpecialityShouldThrowNotFoundEntityExceptionWhenEntityNotFound() {
        when(trainingProgramRepository.findBySpeciality("unknown")).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> trainingProgramService.findBySpeciality("unknown"));
    }

    @Test
    void findById() {
        TrainingProgram trainingProgram = new TrainingProgram();

        when(trainingProgramRepository.findById(1)).thenReturn(Optional.of(trainingProgram));

        trainingProgramService.findById(1);

        InOrder inOrder = inOrder(trainingProgramRepository, trainingProgramMapper);
        inOrder.verify(trainingProgramRepository).findById(1);
        inOrder.verify(trainingProgramMapper).toDto(trainingProgram);

        verifyNoMoreInteractions(trainingProgramRepository, trainingProgramMapper);
    }

    @Test
    void findBySpeciality() {
        TrainingProgram trainingProgram = new TrainingProgram();
        when(trainingProgramRepository.findBySpeciality("program")).thenReturn(Optional.of(trainingProgram));

        trainingProgramService.findBySpeciality("program");

        InOrder inOrder = inOrder(trainingProgramRepository, trainingProgramMapper);
        inOrder.verify(trainingProgramRepository).findBySpeciality("program");
        inOrder.verify(trainingProgramMapper).toDto(trainingProgram);

        verifyNoMoreInteractions(trainingProgramRepository, trainingProgramMapper);

    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<TrainingProgram> trainingPrograms = new PageImpl<>(List.of(new TrainingProgram()));

        when(trainingProgramRepository.findAll(pageable)).thenReturn(trainingPrograms);

        trainingProgramService.findAll(pageable);

        InOrder inOrder = inOrder(trainingProgramRepository, trainingProgramMapper);
        inOrder.verify(trainingProgramRepository).findAll(pageable);
        inOrder.verify(trainingProgramMapper, times(trainingPrograms.getSize())).toDto(any());

        verifyNoMoreInteractions(trainingProgramRepository, trainingProgramMapper);
    }

    @Test
    void add() {
        TrainingProgram adding = new TrainingProgram();
        TrainingProgram added = new TrainingProgram();
        TrainingProgramDto trainingProgramDto = new TrainingProgramDto();

        when(trainingProgramMapper.fromDto(trainingProgramDto)).thenReturn(adding);
        when(trainingProgramRepository.save(adding)).thenReturn(added);

        trainingProgramService.saveOrUpdate(trainingProgramDto);

        InOrder inOrder = inOrder(trainingProgramRepository, trainingProgramMapper);
        inOrder.verify(trainingProgramMapper).fromDto(trainingProgramDto);
        inOrder.verify(trainingProgramRepository).save(adding);
        inOrder.verify(trainingProgramMapper).toDto(added);

        verifyNoMoreInteractions(trainingProgramRepository, trainingProgramMapper);

    }

    @Test
    void update() {
        TrainingProgram updating = new TrainingProgram();
        TrainingProgram updated = new TrainingProgram();
        TrainingProgramDto trainingProgramDto = new TrainingProgramDto();

        when(trainingProgramMapper.fromDto(trainingProgramDto)).thenReturn(updating);
        when(trainingProgramRepository.save(updating)).thenReturn(updated);

        trainingProgramService.saveOrUpdate(trainingProgramDto);

        InOrder inOrder = inOrder(trainingProgramRepository, trainingProgramMapper);
        inOrder.verify(trainingProgramMapper).fromDto(trainingProgramDto);
        inOrder.verify(trainingProgramRepository).save(updating);
        inOrder.verify(trainingProgramMapper).toDto(updated);

        verifyNoMoreInteractions(trainingProgramRepository, trainingProgramMapper);

    }

    @Test
    void delete() {
        trainingProgramService.delete(1);

        Mockito.verify(trainingProgramRepository).deleteById(1);
        verifyNoMoreInteractions(trainingProgramRepository);
    }

    @Test
    void deleteShouldThrowNotFoundEntityExceptionWhenDeletingEntityWhichNotExist(){
        doThrow(NotFoundEntityException.class).when(trainingProgramRepository).deleteById(4);
        assertThrows(NotFoundEntityException.class, ()->trainingProgramService.delete(4));
    }
}