package my.project.university.repository;

import my.project.university.models.Schedule;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface MyScheduleRepository {
    List<Schedule> getScheduleByCriteria(Map<String, String> criteria);
}
