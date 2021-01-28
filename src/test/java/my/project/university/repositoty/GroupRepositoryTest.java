package my.project.university.repositoty;


import my.project.university.models.Group;
import my.project.university.repository.GroupRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import javax.persistence.EntityManager;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ActiveProfiles(profiles = "test")
@Import(TestData.class)
class GroupRepositoryTest {

    @Autowired
    private TestData testData;

    @Autowired
    private EntityManager entityManager;

    @Autowired
    private GroupRepository groupRepository;

    @BeforeEach
    public void fillTestData() {
        testData.fillTestData();
    }

    @Test
    void findByIdShouldReturnEmptyOptionalIfGroupNotFound() {
        assertTrue(groupRepository.findById(4).isEmpty());
    }

    @Test
    void findByDescriptionShouldReturnEmptyOptionalIfGroupNotFound() {
        assertTrue(groupRepository.findByDescription("ddd").isEmpty());
    }


    @Test
    void findByIdShouldReturnOptionalOfGroupWithGivenId() {
        Group actual = groupRepository.findById(1).get();

        assertEquals(testData.group1, actual);
    }

    @Test
    void findByDescriptionShouldReturnOptionalOfCourseWithGivenDescription() {
        Group actual = groupRepository.findByDescription("group_1").get();

        assertEquals(testData.group1, actual);
    }

    @Test
    void findAllShouldReturnAllEntityInDataBase() {
        Page<Group> actual = groupRepository.findAll(PageRequest.of(0,20));
        assertEquals(testData.allGroups, actual.getContent());
    }

    @Test
    void saveShouldAddNewEntityToDataBase() {
        Group adding = new Group("group_4", testData.trainingProgram1);
        Group added = groupRepository.save(adding);

        entityManager.flush();
        entityManager.clear();

        Group actual = groupRepository.findById(4).get();

        assertEquals(adding, actual);
        assertEquals(adding.getId(), actual.getId());
        assertEquals(adding.getTrainingProgram(), actual.getTrainingProgram());

        assertEquals(added, adding);
        assertEquals(added.getId(), adding.getId());
        assertEquals(added.getTrainingProgram(), adding.getTrainingProgram());
    }

    @Test
    void saveShouldUpdateEntityCorrectly() {
        Group updating = new Group(1, "group_10", testData.trainingProgram2);
        Group updated = groupRepository.save(updating);
        updated.getTrainingProgram().getSpeciality();

        entityManager.flush();
        entityManager.clear();

        Group actual = groupRepository.findById(1).get();

        assertEquals(updating, actual);
        assertEquals(updating.getTrainingProgram(), actual.getTrainingProgram());

        assertEquals(updating, updated);
        assertEquals(updating.getTrainingProgram(), updated.getTrainingProgram());
    }

    @Test
    void deleteShouldDeleteGroupWithGivenIdFromDataBase() {
        groupRepository.deleteById(1);

        List<Group> groups = new ArrayList<>();
        groupRepository.findAll().forEach(groups::add);

        assertEquals(2, groups.size());
        assertTrue(groupRepository.findById(1).isEmpty());
    }
}