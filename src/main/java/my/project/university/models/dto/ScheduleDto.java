package my.project.university.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import my.project.university.validation.DateTimeFuture;
import my.project.university.validation.OnCreate;
import my.project.university.validation.OnUpdate;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@Data
@AllArgsConstructor
@NoArgsConstructor
@DateTimeFuture
public class ScheduleDto {

    @NotNull(groups = OnUpdate.class, message = "Id should not be null")
    @Null(groups = OnCreate.class, message = "Id should be null")
    @Positive(message = "Id should be positive")
    private Integer id;


    private String lessonDate;
    private String lessonTime;

    @NotNull(message = "LectureHall id should not be null")
    @Positive(message = "LectureHall id should be positive")
    private Integer lectureHallId;
    private Integer lectureHallHousing;
    private Integer lectureHallFloor;
    private Integer lectureHallNumber;

    @NotBlank(message = "Croup description should not be blank")
    private String groupDescription;

    @NotNull(message = "Teacher id should not be null")
    @Positive(message = "Teacher id should be positive")
    private Integer teacherId;
    private String teacherFirstName;
    private String teacherLastName;

    @NotBlank(message = "Course name should not be blank")
    private String courseName;
}
