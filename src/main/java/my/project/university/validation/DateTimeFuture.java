package my.project.university.validation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.*;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({TYPE})
@Retention(RUNTIME)
@Documented
@Constraint(validatedBy = {DateTimeValidator.class})
public @interface DateTimeFuture {
    String message() default "The specified date and time have already passed";

    Class<?>[] groups() default { };

    Class<? extends Payload>[] payload() default { };
}
