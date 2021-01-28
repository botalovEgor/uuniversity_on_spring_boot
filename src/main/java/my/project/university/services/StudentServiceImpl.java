package my.project.university.services;

import lombok.RequiredArgsConstructor;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.StudentMapper;
import my.project.university.models.Group;
import my.project.university.models.Student;
import my.project.university.models.dto.StudentDto;
import my.project.university.repository.GroupRepository;
import my.project.university.repository.StudentRepository;
import my.project.university.services.interfaces.StudentService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class StudentServiceImpl implements StudentService {
    private final StudentRepository studentRepository;
    private final GroupRepository groupRepository;
    private final StudentMapper mapper;

    private static final String GROUP_TABLE_NAME = "Group";
    private static final String STUDENT_TABLE_NAME = "Student";

    @Override
    public StudentDto findById(Integer id) {
        Student student = studentRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(STUDENT_TABLE_NAME, id));
        return mapper.toDto(student);
    }

    @Override
    public Page<StudentDto> findAll(Pageable pageable) {
        Page<Student> students =studentRepository.findAll(pageable);
        return students.map(mapper::toDto);
    }

    @Override
    public StudentDto saveOrUpdate(StudentDto studentDto) {
        Student student = mapper.fromDto(studentDto);

        Group group = groupRepository.findByDescription(studentDto.getGroupDescription())
                .orElseThrow(() -> new NotFoundEntityException(GROUP_TABLE_NAME, studentDto.getGroupDescription()));

        student.setGroup(group);
        student = studentRepository.save(student);

        group.getStudents().add(student);

        return mapper.toDto(student);
    }

    @Override
    public void delete(Integer id) {
        try {
            studentRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundEntityException(STUDENT_TABLE_NAME, id, e);
        }
    }
}
