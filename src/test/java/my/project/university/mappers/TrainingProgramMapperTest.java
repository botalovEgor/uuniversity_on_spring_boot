package my.project.university.mappers;

import my.project.university.models.TrainingProgram;
import my.project.university.models.dto.TrainingProgramDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TrainingProgramMapperTest {
    private TrainingProgramMapper trainingProgramMapper = new TrainingProgramMapperImpl();
    private TrainingProgram trainingProgram = new TrainingProgram(1, "program");
    private TrainingProgramDto trainingProgramDto = new TrainingProgramDto();
    {
        trainingProgramDto.setId(1);
        trainingProgramDto.setSpeciality("program");
    }

    @Test
    void toDto() {
        assertEquals(trainingProgramDto, trainingProgramMapper.toDto(trainingProgram));
    }

    @Test
    void fromDto() {
        assertEquals(trainingProgram, trainingProgramMapper.fromDto(trainingProgramDto));
    }
}