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
public class GroupDto {

    @NotNull(groups = OnUpdate.class, message = "Id should not be null")
    @Null(groups = OnCreate.class, message = "Id should be null")
    @Positive(message = "Id should be positive")
    private Integer id;

    @NotBlank(message = "Group description should not be blank")
    private String description;

    private Integer trainingProgramId;

    @NotBlank(message = "TrainingProgram speciality should not be blank")
    private String trainingProgram;

}
