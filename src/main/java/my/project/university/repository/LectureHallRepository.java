package my.project.university.repository;

import my.project.university.models.LectureHall;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface LectureHallRepository extends PagingAndSortingRepository<LectureHall, Integer> {

}
