package my.project.university.services.interfaces;

import my.project.university.models.dto.GroupDto;
import my.project.university.models.dto.StudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface GroupService {
    GroupDto findById(Integer id);

    GroupDto findByDescription(String description);

    Page<GroupDto> findAll(Pageable pageable);

    GroupDto saveOrUpdate(GroupDto groupDto);

    void delete(Integer groupId);

    Page<StudentDto> getStudents(Integer id, Pageable pageable);

    void addStudent(Integer groupId, Integer studentId);
}
