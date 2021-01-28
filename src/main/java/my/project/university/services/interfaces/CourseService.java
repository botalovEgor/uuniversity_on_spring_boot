package my.project.university.services.interfaces;

import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.models.dto.TrainingProgramDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CourseService {
    CourseDto findById(Integer id);

    CourseDto findByName(String name);

    Page<CourseDto> findAll(Pageable pageable);

    CourseDto saveOrUpdate(CourseDto courseDto);

    void delete(int id);

    Page<TeacherDto> getTeachers(Integer id, Pageable pageable);

    void deleteTeacher(Integer courseId, Integer teacherId);

    void addTeacher(Integer courseId, Integer teacherId);

    Page<TrainingProgramDto> getTrainingPrograms(Integer id, Pageable pageable);

    void addTrainingProgram(Integer courseId, Integer programId);

    void addTrainingProgram(Integer courseId, String speciality);

    void deleteTrainingProgram(Integer courseId, Integer programId);
}
