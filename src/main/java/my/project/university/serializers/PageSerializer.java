package my.project.university.serializers;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.databind.ser.std.StdSerializer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Sort;

import java.io.IOException;
import java.util.Iterator;

public class PageSerializer extends StdSerializer<Page> {

    public PageSerializer() {
        this(null);
    }

    public PageSerializer(Class<Page> t) {
        super(t);
    }

    @Override
    public void serialize(Page page, JsonGenerator jsonGenerator, SerializerProvider serializerProvider) throws IOException {

        jsonGenerator.writeStartObject();
        jsonGenerator.writeArrayFieldStart("content");
        for (Object o : page.getContent()) {
            jsonGenerator.writeObject(o);
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeNumberField("totalPages", page.getTotalPages());
        jsonGenerator.writeNumberField("totalElements", page.getTotalElements());
        jsonGenerator.writeNumberField("numberOfElements", page.getNumberOfElements());
        jsonGenerator.writeNumberField("pageSize", page.getSize());
        jsonGenerator.writeNumberField("pageNumber", page.getNumber());
        jsonGenerator.writeBooleanField("paged", page.getPageable().isPaged());
        jsonGenerator.writeBooleanField("unPaged", page.getPageable().isUnpaged());
        jsonGenerator.writeBooleanField("lastPage", page.isLast());
        jsonGenerator.writeBooleanField("firstPage", page.isFirst());
        jsonGenerator.writeBooleanField("emptyPage", page.isEmpty());
        jsonGenerator.writeArrayFieldStart("sort");
        Iterator<Sort.Order> iterator = page.getSort().iterator();
        while (iterator.hasNext()) {
            Sort.Order order = iterator.next();
            jsonGenerator.writeStartObject();
            jsonGenerator.writeStringField("property", order.getProperty());
            jsonGenerator.writeBooleanField("ascending", order.isAscending());
            jsonGenerator.writeBooleanField("descending", order.isDescending());
            jsonGenerator.writeBooleanField("ignoreCase", order.isIgnoreCase());
            jsonGenerator.writeStringField("nullHandling", order.getNullHandling().name());
            jsonGenerator.writeEndObject();
        }
        jsonGenerator.writeEndArray();
        jsonGenerator.writeEndObject();

    }

}
