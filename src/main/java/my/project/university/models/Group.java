package my.project.university.models;

import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "groups")
@Data
@NoArgsConstructor
public class Group {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Integer id;

    @Column(name = "description")
    private String description;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "trainingProgram_id")
    @EqualsAndHashCode.Exclude
    private TrainingProgram trainingProgram;

    @OneToMany(mappedBy = "group")
    @Fetch(FetchMode.SUBSELECT)
    @EqualsAndHashCode.Exclude
    @ToString.Exclude
    private Set<Student> students = new HashSet<>();

    public Group(Integer id, String description, TrainingProgram trainingProgram) {
        this.id = id;
        this.description = description;
        this.trainingProgram = trainingProgram;
    }

    public Group(String description, TrainingProgram trainingProgram) {
        this.description = description;
        this.trainingProgram = trainingProgram;
    }

    public Group(Integer id) {
        this.id = id;
    }
}
