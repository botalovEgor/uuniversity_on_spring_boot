package my.project.university.controllers.view;

import lombok.RequiredArgsConstructor;
import my.project.university.models.LectureHall;
import my.project.university.services.interfaces.LectureHallService;
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
import javax.validation.constraints.Positive;

@Controller
@RequestMapping("/lecture-halls")
@Validated
@RequiredArgsConstructor
public class LectureHallsController {
    private static final String ID_CONSTRAINT = "Id should be positive";

    private final LectureHallService lectureHallService;

    @GetMapping("/{id}")
    public String findById(@PathVariable @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        LectureHall lectureHall = lectureHallService.findById(id);
        model.addAttribute("lectureHall", lectureHall);
        return "lectureHalls/lectureHall";
    }

    @GetMapping
    public String showAll(@PageableDefault(sort = "id", direction = Sort.Direction.ASC) Pageable pageable,
                          Model model) {
        Page<LectureHall> page = lectureHallService.findAll(pageable);
        model.addAttribute("page", page);
        return "lectureHalls/lectureHalls";
    }

    @PostMapping
    @Validated(OnCreate.class)
    public String add(@ModelAttribute("newLectureHall") @Valid LectureHall lectureHall) {
        lectureHallService.saveOrUpdate(lectureHall);
        return "redirect:/lecture-halls";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer id, Model model) {
        LectureHall lectureHall = lectureHallService.findById(id);
        model.addAttribute("updateLectureHall", lectureHall);
        return "lectureHalls/update";
    }

    @PostMapping("/update")
    @Validated(OnUpdate.class)
    public String update(@ModelAttribute("updateLectureHall") @Valid LectureHall lectureHall) {
        lectureHallService.saveOrUpdate(lectureHall);
        return "redirect:/lecture-halls";
    }

    @GetMapping("/{id}/delete")
    public String delete(@PathVariable("id") @Positive(message = ID_CONSTRAINT) Integer lectureHallId) {
        lectureHallService.delete(lectureHallId);
        return "redirect:/lecture-halls";
    }
}
