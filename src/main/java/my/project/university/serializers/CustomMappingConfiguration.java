package my.project.university.serializers;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.databind.module.SimpleModule;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.domain.Page;

import java.text.SimpleDateFormat;

@Configuration
public class CustomMappingConfiguration {


    @Bean
    public PageSerializer getPageSerializer(){
        return new PageSerializer();
    }

    @Bean
    public SimpleModule getSimpleModule(){
        SimpleModule module = new SimpleModule();
        module.addSerializer(Page.class, getPageSerializer());
        return module;
    }

    @Bean
    public ObjectMapper getCustomMapper(){
        ObjectMapper mapper = new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT);
        mapper.registerModule(getSimpleModule());
        mapper.setDateFormat(new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:S z"));
        return mapper;
    }
}
