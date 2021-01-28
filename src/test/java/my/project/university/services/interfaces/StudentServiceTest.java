package my.project.university.services.interfaces;

import my.project.university.repository.GroupRepository;
import my.project.university.repository.StudentRepository;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.StudentMapper;
import my.project.university.models.Group;
import my.project.university.models.Student;
import my.project.university.models.dto.StudentDto;
import my.project.university.services.StudentServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InOrder;
import org.mockito.Mockito;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class StudentServiceTest {
    private StudentRepository studentRepository = mock(StudentRepository.class);
    private GroupRepository groupRepository = mock(GroupRepository.class);
    private StudentMapper studentMapper = mock(StudentMapper.class);

    private StudentService studentService = new StudentServiceImpl(studentRepository, groupRepository, studentMapper);

    @Test
    void findByIdShouldThrowNotFoundEntityExceptionWhenEntityNotFound() {
        when(studentRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, () -> studentService.findById(4));
    }

    @Test
    void findById() {
        Student student = new Student();
        Mockito.when(studentRepository.findById(1)).thenReturn(Optional.of(student));

        studentService.findById(1);

        InOrder inOrder = inOrder(studentMapper, studentRepository);
        inOrder.verify(studentRepository).findById(1);
        inOrder.verify(studentMapper).toDto(student);
        verifyNoMoreInteractions(studentRepository, studentMapper);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(1, 10);
        Page<Student> students = new PageImpl<>(List.of(new Student()));

        when(studentRepository.findAll(pageable)).thenReturn(students);

        studentService.findAll(pageable);

        InOrder inOrder = inOrder(studentRepository, studentMapper);
        inOrder.verify(studentRepository).findAll(pageable);
        inOrder.verify(studentMapper, times(students.getSize())).toDto(any());

        verifyNoMoreInteractions(studentRepository, studentMapper);
    }

    @Test
    void add() {
        Student adding = mock(Student.class);
        Student added = new Student();
        StudentDto studentDto = new StudentDto();
        Group group = mock(Group.class);
        Set<Student> students = mock(Set.class);

        when(studentMapper.fromDto(studentDto)).thenReturn(adding);
        when(groupRepository.findByDescription(studentDto.getGroupDescription())).thenReturn(Optional.of(group));
        when(studentRepository.save(adding)).thenReturn(added);
        when(group.getStudents()).thenReturn(students);

        studentService.saveOrUpdate(studentDto);

        InOrder inOrder = inOrder(studentRepository, studentMapper, groupRepository, adding, group, students);
        inOrder.verify(studentMapper).fromDto(studentDto);
        inOrder.verify(groupRepository).findByDescription(studentDto.getGroupDescription());
        inOrder.verify(adding).setGroup(group);
        inOrder.verify(studentRepository).save(adding);
        inOrder.verify(group).getStudents();
        inOrder.verify(students).add(added);
        inOrder.verify(studentMapper).toDto(added);

        verifyNoMoreInteractions(studentRepository, studentMapper, groupRepository, adding, group, students);
    }

    @Test
    void update() {
        Student updating = mock(Student.class);
        Student updated = new Student();
        StudentDto studentDto = new StudentDto();
        Group group = new Group();

        when(studentMapper.fromDto(studentDto)).thenReturn(updating);
        when(groupRepository.findByDescription(studentDto.getGroupDescription())).thenReturn(Optional.of(group));
        when(studentRepository.save(updating)).thenReturn(updated);

        studentService.saveOrUpdate(studentDto);

        InOrder inOrder = inOrder(studentRepository, studentMapper, groupRepository, updating);
        inOrder.verify(studentMapper).fromDto(studentDto);
        inOrder.verify(groupRepository).findByDescription(studentDto.getGroupDescription());
        inOrder.verify(updating).setGroup(group);
        inOrder.verify(studentRepository).save(updating);
        inOrder.verify(studentMapper).toDto(updated);

        verifyNoMoreInteractions(studentRepository, studentMapper, groupRepository, updating);
    }

    @Test
    void delete() {
        studentService.delete(1);

        verify(studentRepository).deleteById(1);
        verifyNoMoreInteractions(studentRepository);
    }

    @Test
    void deleteShouldThrowNotFoundEntityExceptionWhenDeletingEntityWhichNotExist() {
        doThrow(NotFoundEntityException.class).when(studentRepository).deleteById(4);
        assertThrows(NotFoundEntityException.class, () -> studentService.delete(4));
    }
}