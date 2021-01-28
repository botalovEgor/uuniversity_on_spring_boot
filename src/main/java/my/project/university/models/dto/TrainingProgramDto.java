package my.project.university.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.project.university.validation.OnCreate;
import my.project.university.validation.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class TrainingProgramDto {

    @NotNull(groups = OnUpdate.class, message = "Id should not be null")
    @Null(groups = OnCreate.class, message = "Id should be null")
    @Positive(message = "Id should be positive")
    private Integer id;

    @NotBlank(message = "Speciality should not be blank")
    private String speciality;
}
