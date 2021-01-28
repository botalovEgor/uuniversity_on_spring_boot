package my.project.university.property;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@ConfigurationProperties("my")
@Data
public class PropertyClass {
    private String name;
}
