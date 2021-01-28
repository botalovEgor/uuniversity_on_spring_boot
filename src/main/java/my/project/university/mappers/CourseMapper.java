package my.project.university.mappers;


import my.project.university.models.Course;
import my.project.university.models.dto.CourseDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper
public interface CourseMapper {

    CourseDto toDto(Course course);

    @InheritInverseConfiguration
    Course fromDto(CourseDto courseDto);
}
