package my.project.university.services.interfaces;


import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TeacherService {
    TeacherDto findById(Integer id);

    Page<TeacherDto> findAll(Pageable pageable);

    TeacherDto saveOrUpdate(TeacherDto teacherDto);

    void delete(Integer teacherId);

    Page<CourseDto> getCourses(Integer teacherId, Pageable pageable);

    void addCourse(Integer teacherId, Integer courseId);

    void addCourse(Integer teacherId, String courseName);

    void deleteCourse(Integer teacherId, Integer courseId);

}
