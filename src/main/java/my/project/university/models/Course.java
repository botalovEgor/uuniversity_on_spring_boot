package my.project.university.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "courses")
@Data
@NoArgsConstructor
public class Course {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Integer id;

    @Column(name = "name")
    private String name;

    @Column(name = "hours")
    @EqualsAndHashCode.Exclude
    private Integer hours;

    @ManyToMany
    @JoinTable(name = "course_teacher",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "teacher_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    private Set<Teacher> teachers = new HashSet<>();


    @ManyToMany
    @JoinTable(name = "course_trainingProgram",
            joinColumns = @JoinColumn(name = "course_id"),
            inverseJoinColumns = @JoinColumn(name = "trainingProgram_id"))
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    Set<TrainingProgram> trainingPrograms = new HashSet<>();

    public Course(Integer id, String name, Integer hours) {
        this.id = id;
        this.name = name;
        this.hours = hours;
    }

    public Course(String name, Integer hours) {
        this.name = name;
        this.hours = hours;
    }

}
