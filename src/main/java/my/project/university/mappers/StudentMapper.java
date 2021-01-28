package my.project.university.mappers;

import my.project.university.models.Student;
import my.project.university.models.dto.StudentDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface StudentMapper {
    @Mapping(target = "groupDescription", source = "group.description")
    @Mapping(target = "groupId", source = "group.id")
    StudentDto toDto(Student student);

    @InheritInverseConfiguration
    Student fromDto(StudentDto studentDto);
}
