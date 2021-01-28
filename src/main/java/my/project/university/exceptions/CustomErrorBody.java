package my.project.university.exceptions;

import lombok.Data;
import org.springframework.web.context.request.WebRequest;

import java.util.Date;

@Data
public class CustomErrorBody {

    private String message;

    private Date timeStamp = new Date();

    private String path;

    private void setPath(String path) {
        this.path = path.substring(path.indexOf("/"));
    }

    public CustomErrorBody(String message, WebRequest request) {
        this.message = message;
        setPath(request.getDescription(false));

    }
}
