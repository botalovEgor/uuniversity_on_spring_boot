package my.project.university.mappers;

import my.project.university.models.TrainingProgram;
import my.project.university.models.dto.TrainingProgramDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;

@Mapper
public interface TrainingProgramMapper {

    TrainingProgramDto toDto(TrainingProgram trainingProgram);

    @InheritInverseConfiguration
    TrainingProgram fromDto(TrainingProgramDto trainingProgramDto);
}
