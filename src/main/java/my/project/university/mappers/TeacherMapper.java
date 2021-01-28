package my.project.university.mappers;

import my.project.university.models.Teacher;
import my.project.university.models.dto.TeacherDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper
public interface TeacherMapper {

    TeacherDto toDto(Teacher teacher);

    @InheritInverseConfiguration
    Teacher fromDto(TeacherDto teacherDto);
}
