package my.project.university.services;

import lombok.RequiredArgsConstructor;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.CourseMapper;
import my.project.university.mappers.TeacherMapper;
import my.project.university.models.Course;
import my.project.university.models.Teacher;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.repository.CourseRepository;
import my.project.university.repository.TeacherRepository;
import my.project.university.services.interfaces.TeacherService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;


@Service
@Transactional
@RequiredArgsConstructor
public class TeacherServiceImpl implements TeacherService {
    private final TeacherRepository teacherRepository;
    private final CourseRepository courseRepository;
    private final TeacherMapper teacherMapper;
    private final CourseMapper courseMapper;

    private static final String COURSE_TABLE_NAME = "Course";
    private static final String TEACHER_TABLE_NAME = "Teacher";
    private static final String NOT_EXISTS = "This relation is not exists";
    private static final String ALREADY_EXISTS = "This relation is already exists";

    @Override
    public TeacherDto findById(Integer id) {
        Teacher teacher = teacherRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(TEACHER_TABLE_NAME, id));
        return teacherMapper.toDto(teacher);
    }

    @Override
    public Page<TeacherDto> findAll(Pageable pageable) {
        Page<Teacher> teachers = teacherRepository.findAll(pageable);
        return teachers.map(teacherMapper::toDto);
    }

    @Override
    public TeacherDto saveOrUpdate(TeacherDto teacherDto) {
        Teacher teacher = teacherMapper.fromDto(teacherDto);
        teacher = teacherRepository.save(teacher);
        return teacherMapper.toDto(teacher);
    }

    @Override
    public void delete(Integer id) {
        try {
            teacherRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundEntityException(TEACHER_TABLE_NAME, id, e);
        }
    }

    @Override
    public Page<CourseDto> getCourses(Integer teacherId, Pageable pageable) {
        Teacher teacher = teacherRepository.findById(teacherId)
                .orElseThrow(() -> new NotFoundEntityException(TEACHER_TABLE_NAME, teacherId));

        Page<Course> courses = courseRepository.findByTeacher(teacher, pageable);
        return courses.map(courseMapper::toDto);
    }

    @Override
    public void addCourse(Integer teacherId, Integer courseId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new NotFoundEntityException(TEACHER_TABLE_NAME, teacherId);
        }

        if(!courseRepository.existsById(courseId)){
            throw  new NotFoundEntityException(COURSE_TABLE_NAME, courseId);
        }

        int result = teacherRepository.addCourse(teacherId, courseId);
        if (result == 0) {
            throw new IllegalArgumentException(ALREADY_EXISTS);
        }
    }

    @Override
    public void addCourse(Integer teacherId, String courseName) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new NotFoundEntityException(TEACHER_TABLE_NAME, teacherId);
        }

        Course course = courseRepository.findByName(courseName).orElseThrow(()->new NotFoundEntityException(COURSE_TABLE_NAME, courseName));


        int result = teacherRepository.addCourse(teacherId, course.getId());
        if (result == 0) {
            throw new IllegalArgumentException(ALREADY_EXISTS);
        }
    }

    @Override
    public void deleteCourse(Integer teacherId, Integer courseId) {
        if (!teacherRepository.existsById(teacherId)) {
            throw new NotFoundEntityException(TEACHER_TABLE_NAME, teacherId);
        }

        if(!courseRepository.existsById(courseId)){
            throw  new NotFoundEntityException(COURSE_TABLE_NAME, courseId);
        }

        int result = teacherRepository.deleteCourse(teacherId, courseId);
        if (result == 0) {
            throw new IllegalArgumentException(NOT_EXISTS);
        }
    }

}
