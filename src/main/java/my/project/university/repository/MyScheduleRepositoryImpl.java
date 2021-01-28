package my.project.university.repository;

import my.project.university.models.Schedule;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.criteria.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Repository
public class MyScheduleRepositoryImpl implements MyScheduleRepository {
    private static final Logger LOG = LoggerFactory.getLogger(MyScheduleRepositoryImpl.class);
    private final EntityManagerFactory entityManagerFactory;

    @Autowired
    public MyScheduleRepositoryImpl(EntityManagerFactory entityManagerFactory) {
        this.entityManagerFactory = entityManagerFactory;
    }

    @Override
    public List<Schedule> getScheduleByCriteria(Map<String, String> criteria) {
        LOG.info(String.format("Method getScheduleByCriteria with parameters %s", criteria));
        String groupDescription = Optional.ofNullable(criteria.get("groupDescription")).orElse("");
        String teacherId = Optional.ofNullable(criteria.get("teacherId")).orElse("");
        String from = Optional.ofNullable(criteria.get("from")).orElse("");
        String to = Optional.ofNullable(criteria.get("to")).orElse("");


        EntityManager entityManager = entityManagerFactory.createEntityManager();

        CriteriaBuilder builder = entityManager.getCriteriaBuilder();
        CriteriaQuery<Schedule> query = builder.createQuery(Schedule.class);
        Root<Schedule> root = query.from(Schedule.class);
        root.fetch("group");
        root.fetch("course");
        root.fetch("lectureHall");
        root.fetch("teacher");

        query.select(root);

        List<Predicate> predicates = new ArrayList<>();
        if (!groupDescription.equals("")) {
            predicates.add(builder.equal(root.get("group").get("description"), groupDescription));
        }
        if (!teacherId.equals("")) {
            predicates.add(builder.equal(root.get("teacher").get("id"), Integer.parseInt(teacherId)));
        }
        if (!from.equals("")) {
            predicates.add(builder.greaterThanOrEqualTo(root.get("lessonDate"), LocalDate.parse(from)));
        }
        if (!to.equals("")) {
            predicates.add(builder.lessThanOrEqualTo(root.get("lessonDate"), LocalDate.parse(to)));
        }

        query.where(predicates.toArray(Predicate[]::new));

        return entityManager.createQuery(query).getResultStream().collect(Collectors.toList());
    }
}

