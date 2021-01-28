package my.project.university.models;

import lombok.*;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "trainingProgram")
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TrainingProgram {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Integer id;

    @Column(name = "speciality")
    private String speciality;

    @ManyToMany
    @JoinTable(name = "course_trainingProgram",
            joinColumns = @JoinColumn(name = "trainingProgram_id"),
            inverseJoinColumns = @JoinColumn(name = "course_id"))
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Course> courses = new HashSet<>();

    public TrainingProgram(Integer id, String speciality) {
        this.id = id;
        this.speciality = speciality;
    }

    public TrainingProgram(String speciality) {
        this.speciality = speciality;
    }

    public TrainingProgram(Integer id) {
        this.id = id;
    }

}
