package my.project.university.repository;

import my.project.university.models.Course;
import my.project.university.models.Teacher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface TeacherRepository extends PagingAndSortingRepository<Teacher, Integer> {

    @Query("select t from Teacher t inner join t.courses c where c = :course")
    Page<Teacher> findByCourse(@Param("course") Course course, Pageable pageable);

    @Query(value = "insert into course_teacher (course_id, teacher_id) values (:courseId, :teacherId)", nativeQuery = true)
    @Modifying(flushAutomatically = true)
    int addCourse(@Param("teacherId") Integer teacherId, @Param("courseId")Integer courseId);

    @Query(value = "delete from course_teacher where course_id = :courseId and teacher_id = :teacherId", nativeQuery = true)
    @Modifying(flushAutomatically = true)
    int deleteCourse(@Param("teacherId") Integer teacherId, @Param("courseId")Integer courseId);
}
