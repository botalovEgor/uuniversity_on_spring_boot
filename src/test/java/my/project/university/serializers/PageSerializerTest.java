package my.project.university.serializers;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import my.project.university.models.dto.CourseDto;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.json.JsonTest;
import org.springframework.data.domain.*;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringJUnitConfig;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@JsonTest
@ContextConfiguration(classes = CustomMappingConfiguration.class)
@SpringJUnitConfig
class PageSerializerTest {

    @Autowired
    private ObjectMapper mapper;

    @Test
    void serializeShouldTransformPageToJsonCorrectly() throws JsonProcessingException {

        Pageable pageable = PageRequest.of(0, 2, Sort.by("id").ascending());

        List<CourseDto> courses = List.of(new CourseDto(1, "a", 1), new CourseDto(2, "b", 2));

        Page<CourseDto> page = new PageImpl<>(courses, pageable, 3);

        String expected = "{\r\n" +
                "  \"content\" : [ {\r\n" +
                "    \"id\" : 1,\r\n" +
                "    \"name\" : \"a\",\r\n" +
                "    \"hours\" : 1\r\n" +
                "  }, {\r\n" +
                "    \"id\" : 2,\r\n" +
                "    \"name\" : \"b\",\r\n" +
                "    \"hours\" : 2\r\n" +
                "  } ],\r\n" +
                "  \"totalPages\" : 2,\r\n" +
                "  \"totalElements\" : 3,\r\n" +
                "  \"numberOfElements\" : 2,\r\n" +
                "  \"pageSize\" : 2,\r\n" +
                "  \"pageNumber\" : 0,\r\n" +
                "  \"paged\" : true,\r\n" +
                "  \"unPaged\" : false,\r\n" +
                "  \"lastPage\" : false,\r\n" +
                "  \"firstPage\" : true,\r\n" +
                "  \"emptyPage\" : false,\r\n" +
                "  \"sort\" : [ {\r\n" +
                "    \"property\" : \"id\",\r\n" +
                "    \"ascending\" : true,\r\n" +
                "    \"descending\" : false,\r\n" +
                "    \"ignoreCase\" : false,\r\n" +
                "    \"nullHandling\" : \"NATIVE\"\r\n" +
                "  } ]\r\n" +
                "}";

        String actual = mapper.writeValueAsString(page);

        assertEquals(expected, actual);

    }

}