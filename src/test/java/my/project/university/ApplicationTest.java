package my.project.university;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.annotation.Lazy;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT, properties = "my.name=anton")
@ActiveProfiles("test")
class ApplicationTest {

    @Test
    public void loadContext() {

    }
}