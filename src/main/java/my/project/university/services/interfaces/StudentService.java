package my.project.university.services.interfaces;


import my.project.university.models.dto.StudentDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface StudentService {
    StudentDto findById(Integer id);

    Page<StudentDto> findAll(Pageable pageable);

    StudentDto saveOrUpdate(StudentDto studentDto);

    void delete(Integer id);
}
