package my.project.university.controllers.view;


import my.project.university.exceptions.NotFoundEntityException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

import javax.validation.ConstraintViolationException;

@ControllerAdvice(basePackages = "my.project.university.controllers.view")
public class ApplicationExceptionHandler {
    private static final Logger LOG = LoggerFactory.getLogger(ApplicationExceptionHandler.class);


    @ExceptionHandler(NotFoundEntityException.class)
    @ResponseStatus(value = HttpStatus.NOT_FOUND)
    public String handleEntityNotFoundException(NotFoundEntityException e, Model model) {
        LOG.error("handled exception", e);
        model.addAttribute("errorMessage", e.getMessage());
        return "exceptionView/error";
    }

    @ExceptionHandler(IllegalArgumentException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleIllegalArgumentException(IllegalArgumentException e, Model model) {
        LOG.error("handled exception", e);
        model.addAttribute(e.getMessage());
        return "exceptionView/error";
    }

    @ExceptionHandler(DataIntegrityViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleDataIntegrityViolationException(DataIntegrityViolationException e, Model model) {
        LOG.error("handled exception", e);
        model.addAttribute("errorMessage", "Уже существует");
        return "exceptionView/error";
    }

    @ExceptionHandler(BindException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleCBindException(BindException e, Model model) {
        LOG.error("handled exception", e);

        StringBuilder message = new StringBuilder();
        message.append("Restrictions violated:\r\n");

        e.getBindingResult().getAllErrors().forEach(s -> message.append("- ").append(s.getDefaultMessage()).append("\r\n"));

        model.addAttribute("errorMessage", message.toString());
        return "exceptionView/error";
    }

    @ExceptionHandler(ConstraintViolationException.class)
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public String handleConstraintViolationException(ConstraintViolationException e, Model model) {
        LOG.error("handled exception", e);

        StringBuilder message = new StringBuilder();

        e.getConstraintViolations().forEach(s -> message.append("- ").append(s.getMessage()).append("\r\n"));

        model.addAttribute("errorMessage", message.toString());
        return "exceptionView/error";
    }

    @ExceptionHandler(Exception.class)
    @ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
    public String handleException(Exception e, Model model) {
        LOG.error("handled exception", e);
        model.addAttribute("errorMessage", "request execution error");
        return "exceptionView/error";
    }

}
