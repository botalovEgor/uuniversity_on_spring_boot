package my.project.university.controllers.view;


import lombok.RequiredArgsConstructor;
import my.project.university.models.dto.ScheduleDto;
import my.project.university.services.interfaces.ScheduleService;
import my.project.university.validation.OnCreate;
import my.project.university.validation.OnUpdate;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Positive;
import java.util.List;
import java.util.Map;

@Controller
@Validated
@RequestMapping("/schedules")
@RequiredArgsConstructor
public class SchedulesController {
    private static final String ID_CONSTRAINT = "Id should be positive";

    private final ScheduleService scheduleService;

    @GetMapping("/{id}")
    public String findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        ScheduleDto scheduleDto = scheduleService.findById(id);
        model.addAttribute("scheduleDto", scheduleDto);
        return "schedules/schedule";
    }

    @GetMapping
    public String showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                          Model model) {
        Page<ScheduleDto> page = scheduleService.findAll(pageable);
        model.addAttribute("schedules", page);
        return "schedules/schedules";
    }

    @PostMapping
    @Validated(OnCreate.class)
    public String add(@ModelAttribute("newScheduleDto") @Valid ScheduleDto scheduleDto) {
        scheduleService.saveOrUpdate(scheduleDto);
        return "redirect:/schedules";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") Integer id, Model model) {
        ScheduleDto scheduleDto = scheduleService.findById(id);
        model.addAttribute("updateScheduleDto", scheduleDto);
        return "schedules/update";
    }

    @PostMapping("/update")
    @Validated(OnUpdate.class)
    public String update(@ModelAttribute("updateScheduleDto") @Valid ScheduleDto scheduleDto) {
        scheduleService.saveOrUpdate(scheduleDto);
        return "redirect:/schedules";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id) {
        scheduleService.delete(id);
        return "redirect:/schedules";
    }

    @PostMapping("/byCriteria")
    public String getScheduleByCriteria(@RequestParam Map<String, String> filters,
                                        Model model) {
        List<ScheduleDto> schedules = scheduleService.getScheduleByCriteria(filters);
        model.addAttribute("schedules", schedules);
        return "schedules/schedulesByCriteria";
    }
}
