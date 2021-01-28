package my.project.university.controllers.configuration;

import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;

@Configuration
@ConditionalOnProperty(name = "my.name", havingValue = "anton", matchIfMissing = true)
public class EnabledProperty {
    @PostConstruct
    void d(){
        System.out.println("Я создан");
    }
}
