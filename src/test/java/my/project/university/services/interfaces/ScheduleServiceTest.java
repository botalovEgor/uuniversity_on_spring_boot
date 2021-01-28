package my.project.university.services.interfaces;

import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.ScheduleMapper;
import my.project.university.models.*;
import my.project.university.models.dto.ScheduleDto;
import my.project.university.repository.*;
import my.project.university.services.ScheduleServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ScheduleServiceTest {
    private ScheduleRepository scheduleRepository = mock(ScheduleRepository.class);
    private GroupRepository groupRepository = mock(GroupRepository.class);
    private CourseRepository courseRepository = mock(CourseRepository.class);
    private TeacherRepository teacherRepository = mock(TeacherRepository.class);
    private LectureHallRepository lectureHallRepository = mock(LectureHallRepository.class);
    private ScheduleMapper scheduleMapper = mock(ScheduleMapper.class);

    private ScheduleService scheduleService = new ScheduleServiceImpl(scheduleRepository, groupRepository,
            courseRepository, teacherRepository, lectureHallRepository, scheduleMapper);


    @Test
    void findById() {
        Schedule schedule = new Schedule();
        when(scheduleRepository.findById(1)).thenReturn(Optional.of(schedule));

        scheduleService.findById(1);

        InOrder inOrder = inOrder(scheduleRepository, scheduleMapper);
        inOrder.verify(scheduleRepository).findById(1);
        inOrder.verify(scheduleMapper).toDto(schedule);

        verifyNoMoreInteractions(scheduleRepository, scheduleMapper);
    }

    @Test
    void findByIdShouldThrowNotFoundEntityExceptionWhenEntityNotFound() {
        when(scheduleRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> scheduleService.findById(4));
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Schedule> schedules = new PageImpl<>(List.of(new Schedule()));

        when(scheduleRepository.findAll(pageable)).thenReturn(schedules);

        scheduleService.findAll(pageable);

        InOrder inOrder = inOrder(scheduleRepository, scheduleMapper);
        inOrder.verify(scheduleRepository).findAll(pageable);
        inOrder.verify(scheduleMapper, times(schedules.getSize())).toDto(any());

        verifyNoMoreInteractions(scheduleRepository, scheduleMapper);
    }

    @Test
    void add() {
        ScheduleDto scheduleDto = new ScheduleDto();
        Schedule adding = mock(Schedule.class);
        Schedule added = new Schedule();
        Group group = new Group();
        Course course = new Course();
        Teacher teacher = new Teacher();
        LectureHall lectureHall = new LectureHall();

        when(scheduleMapper.fromDto(scheduleDto)).thenReturn(adding);
        when(groupRepository.findByDescription(scheduleDto.getGroupDescription())).thenReturn(Optional.of(group));
        when(courseRepository.findByName(scheduleDto.getCourseName())).thenReturn(Optional.of(course));
        when(teacherRepository.findById(scheduleDto.getTeacherId())).thenReturn(Optional.of(teacher));
        when(lectureHallRepository.findById(scheduleDto.getTeacherId())).thenReturn(Optional.of(lectureHall));
        when(scheduleRepository.save(adding)).thenReturn(added);

        scheduleService.saveOrUpdate(scheduleDto);

        InOrder inOrder = inOrder(groupRepository, courseRepository, teacherRepository, lectureHallRepository, scheduleRepository, scheduleMapper, adding);

        inOrder.verify(scheduleMapper).fromDto(scheduleDto);

        inOrder.verify(groupRepository).findByDescription(scheduleDto.getGroupDescription());
        inOrder.verify(adding).setGroup(group);

        inOrder.verify(courseRepository).findByName(scheduleDto.getCourseName());
        inOrder.verify(adding).setCourse(course);

        inOrder.verify(teacherRepository).findById(scheduleDto.getTeacherId());
        inOrder.verify(adding).setTeacher(teacher);

        inOrder.verify(lectureHallRepository).findById(scheduleDto.getLectureHallId());
        inOrder.verify(adding).setLectureHall(lectureHall);

        inOrder.verify(scheduleRepository).save(adding);
        inOrder.verify(scheduleMapper).toDto(added);

        verifyNoMoreInteractions(groupRepository, courseRepository, teacherRepository, lectureHallRepository, scheduleRepository, scheduleMapper, adding);
    }

    @Test
    void update() {
        ScheduleDto scheduleDto = new ScheduleDto();
        Schedule updating = mock(Schedule.class);
        Schedule updated = new Schedule();
        Group group = new Group();
        Course course = new Course();
        Teacher teacher = new Teacher();
        LectureHall lectureHall = new LectureHall();

        when(scheduleMapper.fromDto(scheduleDto)).thenReturn(updating);
        when(groupRepository.findByDescription(scheduleDto.getGroupDescription())).thenReturn(Optional.of(group));
        when(courseRepository.findByName(scheduleDto.getCourseName())).thenReturn(Optional.of(course));
        when(teacherRepository.findById(scheduleDto.getTeacherId())).thenReturn(Optional.of(teacher));
        when(lectureHallRepository.findById(scheduleDto.getTeacherId())).thenReturn(Optional.of(lectureHall));
        when(scheduleRepository.save(updating)).thenReturn(updated);

        scheduleService.saveOrUpdate(scheduleDto);

        InOrder inOrder = inOrder(groupRepository, courseRepository, teacherRepository, lectureHallRepository, scheduleRepository, scheduleMapper, updating);

        inOrder.verify(scheduleMapper).fromDto(scheduleDto);

        inOrder.verify(groupRepository).findByDescription(scheduleDto.getGroupDescription());
        inOrder.verify(updating).setGroup(group);

        inOrder.verify(courseRepository).findByName(scheduleDto.getCourseName());
        inOrder.verify(updating).setCourse(course);

        inOrder.verify(teacherRepository).findById(scheduleDto.getTeacherId());
        inOrder.verify(updating).setTeacher(teacher);

        inOrder.verify(lectureHallRepository).findById(scheduleDto.getLectureHallId());
        inOrder.verify(updating).setLectureHall(lectureHall);

        inOrder.verify(scheduleRepository).save(updating);
        inOrder.verify(scheduleMapper).toDto(updated);

        verifyNoMoreInteractions(groupRepository, courseRepository, teacherRepository, lectureHallRepository, scheduleRepository, scheduleMapper, updating);
    }

    @Test
    void delete() {
        scheduleService.delete(1);

        verify(scheduleRepository).remove(1);
        verifyNoMoreInteractions(scheduleRepository);
    }

    @Test
    void getScheduleByCriteria() {
        Map<String, String> filters = new HashMap<>();
        List<Schedule> schedules = List.of(new Schedule());

        when(scheduleRepository.getScheduleByCriteria(filters)).thenReturn(schedules);

        scheduleService.getScheduleByCriteria(filters);

        verify(scheduleRepository).getScheduleByCriteria(filters);
        verify(scheduleMapper, times(schedules.size())).toDto(any());
        verifyNoMoreInteractions(scheduleRepository, scheduleMapper);

    }

    @Test
    void deleteShouldThrowNotFoundEntityExceptionWhenDeletingEntityWhichNotExist() {
        doThrow(NotFoundEntityException.class).when(scheduleRepository).remove(4);
        assertThrows(NotFoundEntityException.class, () -> scheduleService.delete(4));
    }
}