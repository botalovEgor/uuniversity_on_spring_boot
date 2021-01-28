package my.project.university.repository;

import my.project.university.models.Group;
import my.project.university.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StudentRepository extends PagingAndSortingRepository<Student, Integer> {

    @EntityGraph(attributePaths = {"group"})
    Page<Student> findAll(Pageable pageable);

    Page<Student> findAllByGroup(Group group, Pageable pageable);
}
