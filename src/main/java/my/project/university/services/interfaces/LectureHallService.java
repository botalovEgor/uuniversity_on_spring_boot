package my.project.university.services.interfaces;

import my.project.university.models.LectureHall;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface LectureHallService {
    LectureHall findById(Integer id);

    Page<LectureHall> findAll(Pageable pageable);

    LectureHall saveOrUpdate(LectureHall lectureHall);

    void delete(Integer lectureHallId);
}
