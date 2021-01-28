package my.project.university.services.interfaces;

import my.project.university.models.dto.ScheduleDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Map;

public interface ScheduleService {

    ScheduleDto findById(Integer id);

    Page<ScheduleDto> findAll(Pageable pageable);

    ScheduleDto saveOrUpdate(ScheduleDto scheduleDtoTo);

    void delete(Integer scheduleId);

    List<ScheduleDto> getScheduleByCriteria(Map<String, String> filters);
}
