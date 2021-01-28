package my.project.university.services;


import lombok.RequiredArgsConstructor;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.CourseMapper;
import my.project.university.mappers.TeacherMapper;
import my.project.university.mappers.TrainingProgramMapper;
import my.project.university.models.Course;
import my.project.university.models.Teacher;
import my.project.university.models.TrainingProgram;
import my.project.university.models.dto.CourseDto;
import my.project.university.models.dto.TeacherDto;
import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.repository.CourseRepository;
import my.project.university.repository.TeacherRepository;
import my.project.university.repository.TrainingProgramRepository;
import my.project.university.services.interfaces.CourseService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class CourseServiceImpl implements CourseService {
    private final CourseRepository courseRepository;
    private final TeacherRepository teacherRepository;
    private final TrainingProgramRepository trainingProgramRepository;

    private final CourseMapper courseMapper;
    private final TeacherMapper teacherMapper;
    private final TrainingProgramMapper trainingProgramMapper;


    private static final String COURSE_TABLE_NAME = "Course";
    private static final String TRAININGPROGRAM_TABLE_NAME = "TrainingProgram";
    private static final String TEACHER_TABLE_NAME = "Teacher";
    private static final String NOT_EXISTS = "This relation is not exists";
    private static final String ALREADY_EXISTS = "This relation is already exists";


    @Override
    public CourseDto findById(Integer id) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(COURSE_TABLE_NAME, id));
        return courseMapper.toDto(course);
    }

    @Override
    public CourseDto findByName(String name) {
        Course course = courseRepository.findByName(name)
                .orElseThrow(() -> new NotFoundEntityException(COURSE_TABLE_NAME, name));
        return courseMapper.toDto(course);
    }

    @Override
    public Page<CourseDto> findAll(Pageable pageable) {
        Page<Course> courses = courseRepository.findAll(pageable);
        return courses.map(courseMapper::toDto);
    }

    @Override
    public CourseDto saveOrUpdate(CourseDto courseDto) {
        Course course = courseMapper.fromDto(courseDto);
        course = courseRepository.save(course);
        return courseMapper.toDto(course);
    }

    @Override
    public void delete(int id) {
        try {
            courseRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundEntityException(COURSE_TABLE_NAME, id, e);
        }
    }

    @Override
    public Page<TeacherDto> getTeachers(Integer id, Pageable pageable) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(COURSE_TABLE_NAME, id));
        Page<Teacher> teachers = teacherRepository.findByCourse(course, pageable);
        return teachers.map(teacherMapper::toDto);
    }

    @Override
    public void addTeacher(Integer courseId, Integer teacherId) {
        if(!courseRepository.existsById(courseId)){
            throw  new NotFoundEntityException(COURSE_TABLE_NAME, courseId);
        }

        if(!teacherRepository.existsById(teacherId)){
            throw  new NotFoundEntityException(TEACHER_TABLE_NAME, teacherId);
        }

        int result = courseRepository.addTeacher(courseId, teacherId);
        if (result==0) {
            throw new IllegalArgumentException(ALREADY_EXISTS);
        }
    }

    @Override
    public void deleteTeacher(Integer courseId, Integer teacherId) {
        if(!courseRepository.existsById(courseId)){
            throw  new NotFoundEntityException(COURSE_TABLE_NAME, courseId);
        }

        if(!teacherRepository.existsById(teacherId)){
            throw  new NotFoundEntityException(TEACHER_TABLE_NAME, teacherId);
        }

        int result = courseRepository.deleteTeacher(courseId, teacherId);
        if (result==0) {
            throw new IllegalArgumentException(NOT_EXISTS);
        }
    }

    @Override
    public Page<TrainingProgramDto> getTrainingPrograms(Integer id, Pageable pageable) {
        Course course = courseRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(COURSE_TABLE_NAME, id));

        Page<TrainingProgram> trainingPrograms = trainingProgramRepository.findByCourse(course, pageable);
        return trainingPrograms.map(trainingProgramMapper::toDto);
    }

    @Override
    public void addTrainingProgram(Integer courseId, Integer programId) {
        if(!courseRepository.existsById(courseId)){
            throw  new NotFoundEntityException(COURSE_TABLE_NAME, courseId);
        }

        if(!trainingProgramRepository.existsById(programId)){
            throw  new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, programId);
        }

        int result = courseRepository.addTrainingProgram(courseId, programId);
        if (result==0) {
            throw new IllegalArgumentException(ALREADY_EXISTS);
        }
    }

    @Override
    public void addTrainingProgram(Integer courseId, String speciality) {
        if(!courseRepository.existsById(courseId)){
            throw  new NotFoundEntityException(COURSE_TABLE_NAME, courseId);
        }

        TrainingProgram trainingProgram = trainingProgramRepository.findBySpeciality(speciality)
                .orElseThrow(()->new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, speciality));

        int result = courseRepository.addTrainingProgram(courseId, trainingProgram.getId());
        if (result==0) {
            throw new IllegalArgumentException(ALREADY_EXISTS);
        }
    }

    @Override
    public void deleteTrainingProgram(Integer courseId, Integer programId) {
        if(!courseRepository.existsById(courseId)){
            throw  new NotFoundEntityException(COURSE_TABLE_NAME, courseId);
        }

        if(!trainingProgramRepository.existsById(programId)){
            throw  new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, programId);
        }

        int result = courseRepository.deleteTrainingProgram(courseId, programId);
        if (result==0) {
            throw new IllegalArgumentException(NOT_EXISTS);
        }
    }

}
