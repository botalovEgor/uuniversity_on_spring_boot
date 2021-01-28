package my.project.university.repository;

import my.project.university.models.Schedule;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ScheduleRepository extends PagingAndSortingRepository<Schedule, Integer>, MyScheduleRepository {

    @Query(value = "SELECT s from Schedule s " +
            "join fetch s.lectureHall " +
            "join fetch s.group " +
            "join fetch s.teacher " +
            "join fetch  s.course")
    Iterable<Schedule> getAll();

    @Query(value = "delete from Schedule s where s.id = :id")
    @Modifying
    void remove(Integer id);

    @EntityGraph(attributePaths = {"group", "lectureHall", "course", "teacher"})
    Page<Schedule> findAll(Pageable pageable);
}
