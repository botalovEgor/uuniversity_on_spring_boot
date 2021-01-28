package my.project.university.mappers;


import my.project.university.models.Schedule;
import my.project.university.models.dto.ScheduleDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface ScheduleMapper {

    @Mapping(target = "lectureHallId", source = "lectureHall.id")
    @Mapping(target = "lectureHallHousing", source = "lectureHall.housing")
    @Mapping(target = "lectureHallFloor", source = "lectureHall.floor")
    @Mapping(target = "lectureHallNumber", source = "lectureHall.number")
    @Mapping(target = "groupDescription", source = "group.description")
    @Mapping(target = "teacherId", source = "teacher.id")
    @Mapping(target = "teacherFirstName", source = "teacher.firstName")
    @Mapping(target = "teacherLastName", source = "teacher.lastName")
    @Mapping(target = "courseName", source = "course.name")
    ScheduleDto toDto(Schedule schedule);

    @InheritInverseConfiguration
    Schedule fromDto(ScheduleDto scheduleDto);

}
