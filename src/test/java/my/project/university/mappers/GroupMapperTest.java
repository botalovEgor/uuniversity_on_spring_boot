package my.project.university.mappers;


import my.project.university.models.Group;
import my.project.university.models.TrainingProgram;
import my.project.university.models.dto.GroupDto;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class GroupMapperTest {
    private GroupMapper groupMapper =new GroupMapperImpl();
    private TrainingProgram trainingProgram = new TrainingProgram(1, "trainingProgram_1");
    private Group group  = new Group(1, "group_1", trainingProgram);
    private GroupDto groupDto = new GroupDto();
    {
        groupDto.setId(1);
        groupDto.setDescription("group_1");
        groupDto.setTrainingProgramId(1);
        groupDto.setTrainingProgram("trainingProgram_1");
    }

    @Test
    void toDto() {
        assertEquals(groupDto, groupMapper.toDto(group));

    }

    @Test
    void fromDto() {
        assertEquals(group, groupMapper.fromDto(groupDto));
    }
}