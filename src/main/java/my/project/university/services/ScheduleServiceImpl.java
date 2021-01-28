package my.project.university.services;


import lombok.RequiredArgsConstructor;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.ScheduleMapper;
import my.project.university.models.*;
import my.project.university.models.dto.ScheduleDto;
import my.project.university.repository.*;
import my.project.university.services.interfaces.ScheduleService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class ScheduleServiceImpl implements ScheduleService {
    private final ScheduleRepository scheduleRepository;
    private final GroupRepository groupRepository;
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final LectureHallRepository lectureHallRepository;
    private final ScheduleMapper mapper;

    private static final String LECTUREHALL_TABLE_NAME = "LectureHall";
    private static final String COURSE_TABLE_NAME = "Course";
    private static final String TEACHER_TABLE_NAME = "Teacher";
    private static final String GROUP_TABLE_NAME = "Group";
    private static final String SCHEDULE_TABLE_NAME = "Schedule";

    @Override
    public ScheduleDto findById(Integer id) {
        Schedule schedule = scheduleRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(SCHEDULE_TABLE_NAME, id));
        return mapper.toDto(schedule);
    }

    @Override
    public Page<ScheduleDto> findAll(Pageable pageable) {
        Page<Schedule> schedules = scheduleRepository.findAll(pageable);
        return schedules.map(mapper::toDto);
    }

    @Override
    public ScheduleDto saveOrUpdate(ScheduleDto scheduleDto) {
        Schedule schedule = mapper.fromDto(scheduleDto);

        Group group = groupRepository.findByDescription(scheduleDto.getGroupDescription())
                .orElseThrow(() -> new NotFoundEntityException(GROUP_TABLE_NAME, scheduleDto.getGroupDescription()));
        schedule.setGroup(group);

        Course course = courseRepository.findByName(scheduleDto.getCourseName())
                .orElseThrow(() -> new NotFoundEntityException(COURSE_TABLE_NAME, scheduleDto.getCourseName()));
        schedule.setCourse(course);

        Teacher teacher = teacherRepository.findById(scheduleDto.getTeacherId())
                .orElseThrow(() -> new NotFoundEntityException(TEACHER_TABLE_NAME, scheduleDto.getTeacherId()));
        schedule.setTeacher(teacher);

        LectureHall lectureHall = lectureHallRepository.findById(scheduleDto.getLectureHallId())
                .orElseThrow(() -> new NotFoundEntityException(LECTUREHALL_TABLE_NAME, scheduleDto.getLectureHallId()));
        schedule.setLectureHall(lectureHall);

        schedule = scheduleRepository.save(schedule);
        return mapper.toDto(schedule);
    }

    @Override
    public void delete(Integer id) {
        try {
            scheduleRepository.remove(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundEntityException(SCHEDULE_TABLE_NAME, id, e);
        }
    }

    @Override
    public List<ScheduleDto> getScheduleByCriteria(Map<String, String> filters) {
        List<Schedule> schedules = scheduleRepository.getScheduleByCriteria(filters);
        return schedules.stream().map(mapper::toDto).collect(Collectors.toList());
    }
}


