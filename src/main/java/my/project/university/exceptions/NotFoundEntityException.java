package my.project.university.exceptions;

public class NotFoundEntityException extends RuntimeException {
    private static final String ERROR_ID_MESSAGE = "%s with id %d not found";
    private static final String ERROR_NAME_MESSAGE = "%s %s not found";

    public NotFoundEntityException(String entityName, Integer id, Throwable cause) {
        super(String.format(ERROR_ID_MESSAGE, entityName, id), cause);
    }

    public NotFoundEntityException(String entityName, String searchParameters, Throwable cause) {
        super(String.format(ERROR_NAME_MESSAGE, entityName, searchParameters), cause);
    }

    public NotFoundEntityException(String entityName, Integer id) {
        super(String.format(ERROR_ID_MESSAGE, entityName, id));
    }

    public NotFoundEntityException(String entityName, String searchParameters) {
        super(String.format(ERROR_NAME_MESSAGE, entityName, searchParameters));
    }
}
