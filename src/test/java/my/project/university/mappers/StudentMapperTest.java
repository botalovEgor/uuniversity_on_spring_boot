package my.project.university.mappers;

import my.project.university.models.Group;
import my.project.university.models.Student;
import my.project.university.models.dto.StudentDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class StudentMapperTest {
    private StudentMapper studentMapper = new StudentMapperImpl();

    private Group group = new Group(1, "group", null);
    private Student student = new Student(1, "name", "name", group);
    private StudentDto studentDto = new StudentDto();
    {
        studentDto.setId(1);
        studentDto.setFirstName("name");
        studentDto.setLastName("name");
        studentDto.setGroupId(1);
        studentDto.setGroupDescription("group");
    }

    @Test
    void toDto() {
        assertEquals(studentDto, studentMapper.toDto(student));
    }

    @Test
    void fromDto() {
        assertEquals(student, studentMapper.fromDto(studentDto));
    }
}