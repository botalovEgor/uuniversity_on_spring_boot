package my.project.university.mappers;

import my.project.university.models.Group;
import my.project.university.models.dto.GroupDto;
import org.mapstruct.InheritInverseConfiguration;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper
public interface GroupMapper {

    @Mapping(target = "trainingProgram", source = "trainingProgram.speciality")
    @Mapping(target = "trainingProgramId", source = "trainingProgram.id")
    GroupDto toDto(Group group);

    @InheritInverseConfiguration
    Group fromDto(GroupDto groupDto);
}
