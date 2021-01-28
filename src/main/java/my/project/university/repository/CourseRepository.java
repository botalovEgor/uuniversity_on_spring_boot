package my.project.university.repository;

import my.project.university.models.Course;
import my.project.university.models.Teacher;
import my.project.university.models.TrainingProgram;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CourseRepository extends PagingAndSortingRepository<Course, Integer> {
    Optional<Course> findByName(String name);

    @Query("select c from Course c inner join c.teachers t where t = :teacher")
    Page<Course> findByTeacher(@Param("teacher") Teacher teacher, Pageable pageable);

    @Query("select c from Course c inner join c.trainingPrograms t where t = :program")
    Page<Course> findByProgram(@Param("program") TrainingProgram trainingProgram, Pageable pageable);

    @Query(value = "insert into course_teacher (course_id, teacher_id) values (:courseId, :teacherId)", nativeQuery = true)
    @Modifying(flushAutomatically = true)
    int addTeacher(@Param("courseId")Integer courseId, @Param("teacherId") Integer teacherId);

    @Query(value = "delete from course_teacher where course_id = :courseId and teacher_id = :teacherId", nativeQuery = true)
    @Modifying(flushAutomatically = true)
    int deleteTeacher(@Param("courseId")Integer courseId, @Param("teacherId") Integer teacherId);

    @Query(value = "insert into course_trainingprogram (course_id, trainingprogram_id) values (:courseId, :programId)", nativeQuery = true)
    @Modifying(flushAutomatically = true)
    int addTrainingProgram(@Param("courseId")Integer courseId, @Param("programId") Integer programId);
    
    @Query(value = "delete from course_trainingprogram where course_id = :courseId and trainingprogram_id = :programId", nativeQuery = true)
    @Modifying(flushAutomatically = true)
    int deleteTrainingProgram(@Param("courseId")Integer courseId, @Param("programId") Integer programId);
    
}
