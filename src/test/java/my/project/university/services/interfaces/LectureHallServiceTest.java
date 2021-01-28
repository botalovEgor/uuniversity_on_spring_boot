package my.project.university.services.interfaces;

import my.project.university.repository.LectureHallRepository;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.models.LectureHall;
import my.project.university.services.LectureHallServiceImpl;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class LectureHallServiceTest {
    private LectureHallRepository lectureHallRepository = mock(LectureHallRepository.class);
    private LectureHallService lectureHallService = new LectureHallServiceImpl(lectureHallRepository);

    private LectureHall lectureHall = new LectureHall(1, 1, 1, 1);

    @Test
    void findByIdShouldThrowNotFoundEntityExceptionWhenEntityNotFound(){
        when(lectureHallRepository.findById(4)).thenReturn(Optional.empty());
        assertThrows(NotFoundEntityException.class, ()->lectureHallService.findById(4));
    }

    @Test
    void findById() {
        when(lectureHallRepository.findById(1)).thenReturn(Optional.of(lectureHall));
        lectureHallService.findById(1);

        verify(lectureHallRepository).findById(1);
        verifyNoMoreInteractions(lectureHallRepository);
    }

    @Test
    void findAll() {
        Pageable pageable = PageRequest.of(0, 10);
        Page<LectureHall> lectureHalls = new PageImpl<>(List.of(new LectureHall()));

        lectureHallService.findAll(pageable);

        verify(lectureHallRepository).findAll(pageable);
        verifyNoMoreInteractions(lectureHallRepository);
    }

    @Test
    void add() {
        lectureHallService.saveOrUpdate(lectureHall);

        verify(lectureHallRepository).save(new LectureHall(lectureHall));
        verifyNoMoreInteractions(lectureHallRepository);
    }

    @Test
    void update() {
        lectureHallService.saveOrUpdate(lectureHall);

        verify(lectureHallRepository).save(lectureHall);
        verifyNoMoreInteractions(lectureHallRepository);
    }

    @Test
    void delete() {
        lectureHallService.delete(1);

        verify(lectureHallRepository).deleteById(1);
        verifyNoMoreInteractions(lectureHallRepository);
    }

    @Test
    void deleteShouldThrowNotFoundEntityExceptionWhenDeletingEntityWhichNotExist(){
        doThrow(NotFoundEntityException.class).when(lectureHallRepository).deleteById(4);
        assertThrows(NotFoundEntityException.class, ()->lectureHallService.delete(4));
    }
}