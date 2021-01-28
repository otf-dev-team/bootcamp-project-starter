package id.co.bni.otf.bootcamp.web.exception;

import org.springframework.http.HttpStatus;

import java.util.*;

/**
 * Default exception response body
 *
 * @since 1.0.0
 * @author efriandika
 */
public class ExceptionResponse {
    private HttpStatus status;
    private String message;
    private List<ExceptionErrorItem> errors;

    public ExceptionResponse(HttpStatus status, String message, List<ExceptionErrorItem> errors) {
        super();
        this.status = status;
        this.message = message;
        this.errors = errors;
    }

    public ExceptionResponse(HttpStatus status, String message, String error) {
        super();
        this.status = status;
        this.message = message;
        this.errors = Collections.singletonList(new ExceptionErrorItem("default", error));
    }

    public ExceptionResponse(HttpStatus status, String message) {
        super();
        this.status = status;
        this.message = message;
        this.errors = new ArrayList<>();
    }

    public HttpStatus getStatus() {
        return status;
    }

    public void setStatus(HttpStatus status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public List<ExceptionErrorItem> getErrors() {
        return errors;
    }

    public void setErrors(List<ExceptionErrorItem> errors) {
        this.errors = errors;
    }
}
