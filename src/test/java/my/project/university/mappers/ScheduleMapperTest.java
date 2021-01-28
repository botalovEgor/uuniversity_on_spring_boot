package my.project.university.mappers;


import my.project.university.models.*;
import my.project.university.models.dto.ScheduleDto;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ScheduleMapperTest {
    private ScheduleMapper scheduleMapper = new ScheduleMapperImpl();
    private Teacher teacher = new Teacher(1, "name", "name");
    private Course course = new Course(null, "course", null);
    private LectureHall lectureHall = new LectureHall(1, 1, 1, 1);
    private Group group = new Group(null, "group", null);
    private Schedule schedule = new Schedule(1, LocalDate.parse("2020-01-01"), LocalTime.parse("01:01"), lectureHall, group, teacher, course);
    private ScheduleDto scheduleDto = new ScheduleDto();
    {
        scheduleDto.setId(1);
        scheduleDto.setLessonDate("2020-01-01");
        scheduleDto.setLessonTime("01:01:00");
        scheduleDto.setLectureHallId(1);
        scheduleDto.setLectureHallHousing(1);
        scheduleDto.setLectureHallFloor(1);
        scheduleDto.setLectureHallNumber(1);
        scheduleDto.setGroupDescription("group");
        scheduleDto.setTeacherId(1);
        scheduleDto.setTeacherFirstName("name");
        scheduleDto.setTeacherLastName("name");
        scheduleDto.setCourseName("course");
    }

    @Test
    void toDto() {
        assertEquals(scheduleDto, scheduleMapper.toDto(schedule));
    }

    @Test
    void fromDto() {
        assertEquals(schedule, scheduleMapper.fromDto(scheduleDto));
    }
}