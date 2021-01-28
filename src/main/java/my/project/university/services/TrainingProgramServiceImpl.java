package my.project.university.services;

import lombok.RequiredArgsConstructor;
import my.project.university.mappers.CourseMapper;
import my.project.university.models.Course;
import my.project.university.models.Teacher;
import my.project.university.models.dto.CourseDto;
import my.project.university.repository.CourseRepository;
import my.project.university.repository.TrainingProgramRepository;
import my.project.university.exceptions.NotFoundEntityException;
import my.project.university.mappers.TrainingProgramMapper;
import my.project.university.models.TrainingProgram;
import my.project.university.models.dto.TrainingProgramDto;
import my.project.university.services.interfaces.TrainingProgramService;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
@RequiredArgsConstructor
public class TrainingProgramServiceImpl implements TrainingProgramService {

    private final TrainingProgramRepository trainingProgramRepository;
    private final CourseRepository courseRepository;
    private final TrainingProgramMapper mapper;
    private final CourseMapper courseMapper;

    private static final String TRAININGPROGRAM_TABLE_NAME = "TrainingProgram";
    private static final String COURSE_TABLE_NAME = "Course";
    private static final String NOT_EXISTS = "This relation is not exists";
    private static final String ALREADY_EXISTS = "This relation is already exists";

    @Override
    public TrainingProgramDto findById(Integer id) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, id));
        return mapper.toDto(trainingProgram);
    }

    @Override
    public TrainingProgramDto findBySpeciality(String speciality) {
        TrainingProgram trainingProgram = trainingProgramRepository.findBySpeciality(speciality)
                .orElseThrow(() -> new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, speciality));
        return mapper.toDto(trainingProgram);
    }

    @Override
    public Page<TrainingProgramDto> findAll(Pageable pageable) {
        Page<TrainingProgram> trainingPrograms = trainingProgramRepository.findAll(pageable);
        return trainingPrograms.map(mapper::toDto);
    }

    @Override
    public TrainingProgramDto saveOrUpdate(TrainingProgramDto trainingProgramDto) {
        TrainingProgram trainingProgram = mapper.fromDto(trainingProgramDto);
        trainingProgram = trainingProgramRepository.save(trainingProgram);
        return mapper.toDto(trainingProgram);
    }

    @Override
    public void delete(Integer id) {
        try {
            trainingProgramRepository.deleteById(id);
        } catch (EmptyResultDataAccessException e) {
            throw new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, id, e);
        }
    }

    @Override
    public Page<CourseDto> getCourses(Integer id, Pageable pageable) {
        TrainingProgram trainingProgram = trainingProgramRepository.findById(id)
                .orElseThrow(() -> new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, id));

        Page<Course> courses = courseRepository.findByProgram(trainingProgram, pageable);
        return courses.map(courseMapper::toDto);
    }

    @Override
    public void addCourse(Integer programId, Integer courseId) {
        if(!trainingProgramRepository.existsById(programId)){
            throw  new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, programId);
        }

        if(!courseRepository.existsById(courseId)){
            throw  new NotFoundEntityException(COURSE_TABLE_NAME, courseId);
        }


        int result = trainingProgramRepository.addCourse(programId, courseId);
        if (result==0) {
            throw new IllegalArgumentException(ALREADY_EXISTS);
        }
    }

    @Override
    public void addCourse(Integer programId, String courseName) {
        if(!trainingProgramRepository.existsById(programId)){
            throw  new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, programId);
        }

        Course course = courseRepository.findByName(courseName).orElseThrow(()-> new NotFoundEntityException(COURSE_TABLE_NAME, courseName));

        int result = trainingProgramRepository.addCourse(programId, course.getId());
        if (result==0) {
            throw new IllegalArgumentException(ALREADY_EXISTS);
        }
    }

    @Override
    public void deleteCourse(Integer programId, Integer courseId) {
        if(!trainingProgramRepository.existsById(programId)){
            throw  new NotFoundEntityException(TRAININGPROGRAM_TABLE_NAME, programId);
        }

        if(!courseRepository.existsById(courseId)){
            throw  new NotFoundEntityException(COURSE_TABLE_NAME, courseId);
        }


        int result = trainingProgramRepository.deleteCourse(programId, courseId);
        if (result==0) {
            throw new IllegalArgumentException(NOT_EXISTS);
        }
    }
}
