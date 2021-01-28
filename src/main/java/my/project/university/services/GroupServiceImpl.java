package my.project.university.services;


import lombok.RequiredArgsConstructor;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.GroupMapper;
import my.project.university.mappers.StudentMapper;
import my.project.university.models.*;
import my.project.university.models.dto.GroupDto;
import my.project.university.models.dto.StudentDto;
import my.project.university.repository.GroupRepository;
import my.project.university.repository.StudentRepository;
import my.project.university.repository.TrainingProgramRepository;
import my.project.university.services.interfaces.GroupService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class GroupServiceImpl implements GroupService {
    private final GroupRepository groupRepository;
    private final TrainingProgramRepository trainingProgramRepository;
    private final StudentRepository studentRepository;
    private final StudentMapper studentMapper;
    private final GroupMapper groupMapper;

    private static final String GROUP_TABLE_NAME = "Group";
    private static final String TRAININGPROGRAM_TABLE_NAME = "TrainingProgram";
    private static final String STUDENT_TABLE_NAME = "Student";

    @Override
    public GroupDto findById(Integer id) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(GROUP_TABLE_NAME, id));
        return groupMapper.toDto(group);
    }

    @Override
    public GroupDto findByDescription(String description) {
        Group group = groupRepository.findByDescription(description)
                .orElseThrow(() -> new NotFoundEntityException(GROUP_TABLE_NAME, description));
        return groupMapper.toDto(group);
    }

    @Override
    public Page<GroupDto> findAll(Pageable pageable) {
        Page<Group> groups = groupRepository.findAll(pageable);
        return groups.map(groupMapper::toDto);
    }

    @Override
    public GroupDto saveOrUpdate(GroupDto groupDto) {
        Group group = groupMapper.fromDto(groupDto);

        TrainingProgram trainingProgram = trainingProgramRepository.findBySpeciality(groupDto.getTrainingProgram())
                .orElseThrow(() -> new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, groupDto.getTrainingProgram()));

        group.setTrainingProgram(trainingProgram);

        group = groupRepository.save(group);

        return groupMapper.toDto(group);
    }

    @Override
    public void delete(Integer id) {
        try {
            groupRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundEntityException(GROUP_TABLE_NAME, id, e);
        }
    }

    @Override
    public Page<StudentDto> getStudents(Integer id, Pageable pageable) {
        Group group = groupRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(GROUP_TABLE_NAME, id));
        Page<Student> students = studentRepository.findAllByGroup(group, pageable);
        return students.map(studentMapper::toDto);
    }

    @Override
    public void addStudent(Integer groupId, Integer studentId) {
        Group group = groupRepository.findById(groupId)
                .orElseThrow(() -> new NotFoundEntityException(GROUP_TABLE_NAME, groupId));

        Student student = studentRepository.findById(studentId).orElseThrow(() -> new NotFoundEntityException(STUDENT_TABLE_NAME, studentId));

        student.setGroup(group);
    }
}
