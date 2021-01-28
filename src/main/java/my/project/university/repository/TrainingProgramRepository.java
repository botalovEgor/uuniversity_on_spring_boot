package my.project.university.repository;

import my.project.university.models.Course;
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
public interface TrainingProgramRepository extends PagingAndSortingRepository<TrainingProgram, Integer> {
    Optional<TrainingProgram> findBySpeciality(String speciality);

    @Query("select t from TrainingProgram t inner join t.courses c where c = :course")
    Page<TrainingProgram> findByCourse(@Param("course") Course course, Pageable pageable);

    @Query(value = "insert into course_trainingprogram (course_id, trainingprogram_id) values (:courseId, :programId)", nativeQuery = true)
    @Modifying(flushAutomatically = true)
    int addCourse(@Param("programId") Integer programId, @Param("courseId")Integer courseId);

    @Query(value = "delete from course_trainingprogram where course_id = :courseId and trainingprogram_id = :programId", nativeQuery = true)
    @Modifying(flushAutomatically = true)
    int deleteCourse(@Param("programId") Integer programId, @Param("courseId")Integer courseId);
}
