package my.project.university.models;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;


@Entity
@Table(name = "schedule")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @EqualsAndHashCode.Exclude
    private Integer id;

    @Column(name = "lesson_date")
    private LocalDate lessonDate;

    @Column(name = "lesson_time")
    private LocalTime lessonTime;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "lectureHall_id")
    private LectureHall lectureHall;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "group_id")
    private Group group;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "teacher_id")
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY)
    @Fetch(FetchMode.JOIN)
    @JoinColumn(name = "course_id")
    private Course course;

    public Schedule(LocalDate lessonDate, LocalTime lessonTime, LectureHall lectureHall, Group group, Teacher teacher, Course course) {
        this.lessonDate = lessonDate;
        this.lessonTime = lessonTime;
        this.lectureHall = lectureHall;
        this.group = group;
        this.teacher = teacher;
        this.course = course;
    }
}
