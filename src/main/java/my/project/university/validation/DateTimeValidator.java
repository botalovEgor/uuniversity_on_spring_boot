package my.project.university.validation;

import my.project.university.models.dto.ScheduleDto;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeValidator implements ConstraintValidator<DateTimeFuture, ScheduleDto> {
    @Override
    public void initialize(DateTimeFuture constraintAnnotation) {

    }

    @Override
    public boolean isValid(ScheduleDto dto, ConstraintValidatorContext context) {
        LocalDate date = LocalDate.parse(dto.getLessonDate());
        LocalTime time = LocalTime.parse(dto.getLessonTime());
        if(date.isBefore(LocalDate.now())){
            return false;
        } else if(date.isAfter(LocalDate.now())){
            return true;
        } else if(time.isAfter(LocalTime.now())){
            return true;
        } else {
            return false;
        }
    }
}
