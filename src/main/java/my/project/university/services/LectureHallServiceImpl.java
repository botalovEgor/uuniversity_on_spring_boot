package my.project.university.services;

import lombok.RequiredArgsConstructor;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.models.Course;
import my.project.university.models.LectureHall;
import my.project.university.models.dto.CourseDto;
import my.project.university.repository.LectureHallRepository;
import my.project.university.services.interfaces.LectureHallService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Service
@Transactional
@RequiredArgsConstructor
public class LectureHallServiceImpl implements LectureHallService {
    private final LectureHallRepository lectureHallRepository;

    private static final String LECTUREHALL_TABLE_NAME = "LectureHall";

    @Override
    public LectureHall findById(Integer id) {
        return lectureHallRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(LECTUREHALL_TABLE_NAME, id));
    }

    @Override
    public Page<LectureHall> findAll(Pageable pageable) {
        return lectureHallRepository.findAll(pageable);
    }

    @Override
    public LectureHall saveOrUpdate(LectureHall lectureHall) {
        LectureHall adding = new LectureHall(lectureHall);
        return lectureHallRepository.save(adding);
    }

    @Override
    public void delete(Integer id) {
        try {
            lectureHallRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundEntityException(LECTUREHALL_TABLE_NAME, id, e);
        }
    }
}
