package my.project.university.mappers;

import my.project.university.models.Course;
import my.project.university.models.dto.CourseDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CourseMapperTest {
    private CourseMapper courseMapper = new CourseMapperImpl();
    private Course course = new Course(1, "any", 100);
    private CourseDto courseDto = new CourseDto();
    {
        courseDto.setId(1);
        courseDto.setName("any");
        courseDto.setHours(100);
    }

    @Test
    void toDto() {
        assertEquals(courseDto, courseMapper.toDto(course));

    }

    @Test
    void fromDto() {
        assertEquals(course, courseMapper.fromDto(courseDto));
    }
}