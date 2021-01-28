package my.project.university.repository;

import my.project.university.models.Group;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface GroupRepository extends PagingAndSortingRepository<Group, Integer> {
    Optional<Group> findByDescription(String description);
}
