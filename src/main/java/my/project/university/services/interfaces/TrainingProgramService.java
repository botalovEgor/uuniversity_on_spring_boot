package my.project.university.services.interfaces;

import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TrainingProgramDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TrainingProgramService {

    TrainingProgramDto findById(Integer id);

    TrainingProgramDto findBySpeciality(String speciality);

    Page<TrainingProgramDto> findAll(Pageable pageable);

    TrainingProgramDto saveOrUpdate(TrainingProgramDto trainingProgram);

    void delete(Integer trainingProgramId);

    Page<CourseDto> getCourses(Integer id, Pageable pageable);

    void addCourse(Integer programId, Integer courseId);

    void addCourse(Integer programId, String courseName);

    void deleteCourse(Integer programId, Integer courseId);
}
