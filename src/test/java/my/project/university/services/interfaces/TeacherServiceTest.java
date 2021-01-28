package my.project.university.services.interfaces;

import my.project.university.mappers.CourseMapper;
import my.project.university.repository.CourseRepository;
import my.project.university.repository.TeacherRepository;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.TeacherMapper;
import my.project.university.models.Course;
import my.project.university.models.Teacher;
import my.project.university.models.dto.TeacherDto;
import my.project.university.services.TeacherServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class TeacherServiceTest {
    private TeacherRepository teacherRepository = mock(TeacherRepository.class);
    private CourseRepository courseRepository = mock(CourseRepository.class);
    private TeacherMapper teacherMapper = mock(TeacherMapper.class);
    private CourseMapper courseMapper = mock(CourseMapper.class);

    private TeacherService teacherService = new TeacherServiceImpl(teacherRepository, courseRepository,
            teacherMapper, courseMapper);

    @Test
    void findByIdShouldThrowNotFoundEntityExceptionWhenEntityNotFound() {
        when(teacherRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> teacherService.findById(4));
    }

    @Test
    void findById() {
        Teacher teacher = new Teacher();
        when(teacherRepository.findById(1)).thenReturn(Optional.of(teacher));

        teacherService.findById(1);

        InOrder inOrder = inOrder(teacherRepository, teacherMapper);
        inOrder.verify(teacherRepository).findById(1);
        inOrder.verify(teacherMapper).toDto(teacher);

        verifyNoMoreInteractions(teacherRepository, teacherMapper);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Teacher> teachers = new PageImpl<>(List.of(new Teacher()));

        when(teacherRepository.findAll(pageable)).thenReturn(teachers);

        teacherService.findAll(pageable);

        InOrder inOrder = inOrder(teacherRepository, teacherMapper);
        inOrder.verify(teacherRepository).findAll(pageable);
        inOrder.verify(teacherMapper, times(teachers.getSize())).toDto(any());

        verifyNoMoreInteractions(teacherRepository, teacherMapper);
    }

    @Test
    void add() {
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = new TeacherDto();

        when(teacherMapper.fromDto(teacherDto)).thenReturn(teacher);
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        teacherService.saveOrUpdate(teacherDto);

        InOrder inOrder = inOrder(teacherRepository, teacherMapper);
        inOrder.verify(teacherMapper).fromDto(teacherDto);
        inOrder.verify(teacherRepository).save(teacher);
        inOrder.verify(teacherMapper).toDto(teacher);

        verifyNoMoreInteractions(teacherRepository, teacherMapper);
    }

    @Test
    void update() {
        Teacher teacher = new Teacher();
        TeacherDto teacherDto = new TeacherDto();

        when(teacherMapper.fromDto(teacherDto)).thenReturn(teacher);
        when(teacherRepository.save(teacher)).thenReturn(teacher);

        teacherService.saveOrUpdate(teacherDto);

        InOrder inOrder = inOrder(teacherRepository, teacherMapper);
        inOrder.verify(teacherMapper).fromDto(teacherDto);
        inOrder.verify(teacherRepository).save(teacher);
        inOrder.verify(teacherMapper).toDto(teacher);

        verifyNoMoreInteractions(teacherRepository, teacherMapper);
    }

    @Test
    void delete() {
        teacherService.delete(1);

        verify(teacherRepository).deleteById(1);
        verifyNoMoreInteractions(teacherRepository);
    }

    @Test
    void deleteShouldThrowNotFoundEntityExceptionWhenDeletingEntityWhichNotExist() {
        doThrow(NotFoundEntityException.class).when(teacherRepository).deleteById(4);
        assertThrows(NotFoundEntityException.class, () -> teacherService.delete(4));
    }
}