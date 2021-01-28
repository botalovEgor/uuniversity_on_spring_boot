package my.project.university.services.interfaces;

import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.CourseMapper;
import my.project.university.mappers.TeacherMapper;
import my.project.university.mappers.TrainingProgramMapper;
import my.project.university.models.Course;
import my.project.university.models.Teacher;
import my.project.university.models.TrainingProgram;
import my.project.university.models.dto.CourseDto;
import my.project.university.repository.CourseRepository;
import my.project.university.repository.TeacherRepository;
import my.project.university.repository.TrainingProgramRepository;
import my.project.university.services.CourseServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.data.domain.*;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;


class CourseServiceTest {
    private final CourseRepository courseRepository = mock(CourseRepository.class);
    private final TeacherRepository teacherRepository = mock(TeacherRepository.class);
    private final TrainingProgramRepository trainingProgramRepository = mock(TrainingProgramRepository.class);

    private final CourseMapper courseMapper = mock(CourseMapper.class);
    private final TeacherMapper teacherMapper = mock(TeacherMapper.class);
    private final TrainingProgramMapper trainingProgramMapper = mock(TrainingProgramMapper.class);

    private CourseService courseService = new CourseServiceImpl(courseRepository, teacherRepository,
            trainingProgramRepository, courseMapper, teacherMapper, trainingProgramMapper);

    private Set<Course> courses = new HashSet<>();

    {
        courses.add(new Course());
        courses.add(new Course());
    }

    @Test
    void findByIdShouldThrowNotFoundEntityExceptionWhenEntityNotFound(){
        when(courseRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, ()->courseService.findById(4));
    }

    @Test
    void findByNameShouldThrowNotFoundEntityExceptionWhenEntityNotFound(){
        when(courseRepository.findByName("unknown")).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, ()->courseService.findByName("unknown"));
    }

    @Test
    void findById() {
        Course course = new Course();

        when(courseRepository.findById(1)).thenReturn(Optional.of(course));

        courseService.findById(1);

        InOrder inOrder = inOrder(courseRepository, courseMapper);
        inOrder.verify(courseRepository).findById(1);
        inOrder.verify(courseMapper).toDto(course);

        verifyNoMoreInteractions(courseRepository, courseMapper);
    }

    @Test
    void findByName() {
        Course course = new Course();
        when(courseRepository.findByName("course_1")).thenReturn(Optional.of(course));

        courseService.findByName("course_1");

        InOrder inOrder = inOrder(courseRepository, courseMapper);
        inOrder.verify(courseRepository).findByName("course_1");
        inOrder.verify(courseMapper).toDto(course);

        verifyNoMoreInteractions(courseRepository, courseMapper);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Course> courses = new PageImpl<>(List.of(new Course()));

        when(courseRepository.findAll(pageable)).thenReturn(courses);

        courseService.findAll(pageable);

        InOrder inOrder = inOrder(courseRepository, courseMapper);
        inOrder.verify(courseRepository).findAll(pageable);
        inOrder.verify(courseMapper, times(courses.getSize())).toDto(any());

        verifyNoMoreInteractions(courseRepository, courseMapper);
    }

    @Test
    void add() {
        Course adding = new Course();
        Course added = new Course();
        CourseDto courseDto = new CourseDto();

        when(courseMapper.fromDto(courseDto)).thenReturn(adding);
        when(courseRepository.save(adding)).thenReturn(added);

        courseService.saveOrUpdate(courseDto);

        InOrder inOrder = inOrder(courseRepository, courseMapper);
        inOrder.verify(courseMapper).fromDto(courseDto);
        inOrder.verify(courseRepository).save(adding);
        inOrder.verify(courseMapper).toDto(added);

        verifyNoMoreInteractions(courseRepository, courseMapper);
    }

    @Test
    void update() {
        Course updating = new Course();
        Course updated = new Course();
        CourseDto courseDto = new CourseDto();

        when(courseMapper.fromDto(courseDto)).thenReturn(updating);
        when(courseRepository.save(updating)).thenReturn(updated);

        courseService.saveOrUpdate(courseDto);

        InOrder inOrder = inOrder(courseRepository, courseMapper);
        inOrder.verify(courseMapper).fromDto(courseDto);
        inOrder.verify(courseRepository).save(updating);
        inOrder.verify(courseMapper).toDto(updated);

        verifyNoMoreInteractions(courseRepository, courseMapper);
    }

    @Test
    void delete() {
        courseService.delete(1);

        verify(courseRepository).deleteById(1);
        verifyNoMoreInteractions(courseRepository);
    }

    @Test
    void deleteShouldThrowNotFoundEntityExceptionWhenDeletingEntityWhichNotExist(){
        doThrow(NotFoundEntityException.class).when(courseRepository).deleteById(4);
        assertThrows(NotFoundEntityException.class, ()->courseService.delete(4));
    }

}