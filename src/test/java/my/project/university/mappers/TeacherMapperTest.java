package my.project.university.mappers;

import my.project.university.models.Teacher;
import my.project.university.models.dto.TeacherDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TeacherMapperTest {
    private TeacherMapper teacherMapper = new TeacherMapperImpl();

    private Teacher teacher = new Teacher(1, "name", "name");
    private TeacherDto teacherDto = new TeacherDto();
    {
        teacherDto.setId(1);
        teacherDto.setFirstName("name");
        teacherDto.setLastName("name");
    }

    @Test
    void toDto() {
        assertEquals(teacherDto, teacherMapper.toDto(teacher));
    }

    @Test
    void fromDto() {
        assertEquals(teacher, teacherMapper.fromDto(teacherDto));
    }
}