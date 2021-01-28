package my.project.university.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import my.project.university.validation.OnCreate;
import my.project.university.validation.OnUpdate;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Null;
import javax.validation.constraints.Positive;

@Entity
@Table(name = "lectureHalls")
@Data
@NoArgsConstructor
public class LectureHall {
    private static final String ID_CONSTRAINT_UPDATE = "Id should not be null";
    private static final String ID_CONSTRAINT_CREATE = "Id should be null";
    private static final String DESCRIPTION_CONSTRAINT = "Group description should not be blank";
    private static final String TRAININGPROGRAM_CONSRAINT = "TrainingProgram speciality should not be blank";
    private static final String ID_POSITIVE_CONSTRAINT = "Id should be positive";
    private static final String HOUSING_CONSTRAINT_NOT_NULL = "Housing should not be null";
    private static final String HOUSING_POSITIVE_CONSTRAINT = "Housing should be positive";
    private static final String FLOOR_CONSTRAINT_NOT_NULL = "Floor should not be null";
    private static final String FLOOR_POSITIVE_CONSTRAINT = "Floor should be positive";
    private static final String NUMBER_CONSTRAINT_NOT_NULL = "Number should not be null";
    private static final String NUMBER_POSITIVE_CONSTRAINT = "Number should be positive";

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    @NotNull(groups = OnUpdate.class, message = ID_CONSTRAINT_UPDATE)
    @Null(groups = OnCreate.class, message = ID_CONSTRAINT_CREATE)
    @Positive(message = ID_POSITIVE_CONSTRAINT)
    private Integer id;

    @Column(name = "housing")
    @NotNull(message = HOUSING_CONSTRAINT_NOT_NULL)
    @Positive(message = HOUSING_POSITIVE_CONSTRAINT)
    private Integer housing;

    @Column(name = "floor")
    @NotNull(message = FLOOR_CONSTRAINT_NOT_NULL)
    @Positive(message = FLOOR_POSITIVE_CONSTRAINT)
    private Integer floor;

    @Column(name = "number")
    @NotNull(message = NUMBER_CONSTRAINT_NOT_NULL)
    @Positive(message = NUMBER_POSITIVE_CONSTRAINT)
    private Integer number;

    public LectureHall(Integer id, Integer housing, Integer floor, Integer number) {
        this.id = id;
        this.housing = housing;
        this.floor = floor;
        this.number = number;
    }

    public LectureHall(LectureHall lectureHall){
        this(lectureHall.id, lectureHall.housing, lectureHall.floor, lectureHall.number);
    }
}
