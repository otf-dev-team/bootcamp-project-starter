package id.co.bni.otf.bootcamp.web.exception;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.BindException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import java.util.*;

/**
 * Managing all of controller error response
 *
 * @author efriandika
 * @since 1.0.0
 */
@ControllerAdvice
@Order
public class DefaultExceptionHandler extends ResponseEntityExceptionHandler {

    private final Logger log = LoggerFactory.getLogger(DefaultExceptionHandler.class);

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("Method Argument Not Valid [User = {}]", getLoggedInUser(request));

        List<ExceptionErrorItem> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ExceptionErrorItem(error.getField(), error.getDefaultMessage()));
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(new ExceptionErrorItem(error.getObjectName(), error.getDefaultMessage()));
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(), errors);

        return handleExceptionInternal(ex, exceptionResponse, headers, exceptionResponse.getStatus(), request);
    }

    @Override
    protected ResponseEntity<Object> handleHttpRequestMethodNotSupported(HttpRequestMethodNotSupportedException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        StringBuilder builder = new StringBuilder();
        builder.append(ex.getMethod());
        builder.append("Method is not supported for this request. Supported methods are ");

        Objects.requireNonNull(ex.getSupportedHttpMethods()).forEach(t -> builder.append(t).append(" "));

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.METHOD_NOT_ALLOWED, ex.getLocalizedMessage(), builder.toString());

        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }

    // 401
    @ExceptionHandler({
        AuthenticationException.class,
        AuthenticationCredentialsNotFoundException.class,
        BadCredentialsException.class,
        UsernameNotFoundException.class
    })
    public ResponseEntity<Object> handleBadCredentialsException(final Exception ex, final WebRequest request) {
        log.info("Bad Credentials Access [User = {}]", getLoggedInUser(request));
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNAUTHORIZED, ex.getLocalizedMessage());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }

    // 403
    @ExceptionHandler({
        AccessDeniedException.class
    })
    public ResponseEntity<Object> handleAccessDeniedException(final Exception ex, final WebRequest request) {
        log.info("Forbidden Access [User = {}]", getLoggedInUser(request));
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.FORBIDDEN, ex.getLocalizedMessage());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }

    /**
     * Default Data Not Found Handler
     * HTTP Status: 404
     *
     * @param ex Default starter data not found exception
     * @param request Spring MVC Request
     * @return Formatted Response Body
     */
    @ExceptionHandler({
        DataNotFoundException.class
    })
    public ResponseEntity<Object> handleResourceNotFoundException(final Exception ex, final WebRequest request) {
        log.info("Resources Not Found [User = {}]", getLoggedInUser(request));
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.NOT_FOUND, ex.getLocalizedMessage());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }

    /**
     * Default Data Not Unique Handler
     * HTTP Status: 422
     *
     * @param ex Default starter data not found exception
     * @param request Spring MVC Request
     * @return Formatted Response Body
     */
    @ExceptionHandler({
            DataNotUniqueException.class
    })
    public ResponseEntity<Object> handleResourceNotUniqueException(final Exception ex, final WebRequest request) {
        log.info("Resources Not Unique [User = {}]", getLoggedInUser(request));
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }

    /**
     * To handle request body / DTO validation
     * HTTP Status: 422
     *
     * @param ex BindException (by Spring Validation)
     * @param headers Default HTTP Header
     * @param status Default HTTP Status
     * @param request Spring MVC Request
     *
     * @return Formatted Response Body
     */
    @Override
    protected ResponseEntity<Object> handleBindException(BindException ex, HttpHeaders headers, HttpStatus status, WebRequest request) {
        log.info("[Validation] Bind Exception [User = {}]", getLoggedInUser(request));

        List<ExceptionErrorItem> errors = new ArrayList<>();

        for (FieldError error : ex.getBindingResult().getFieldErrors()) {
            errors.add(new ExceptionErrorItem(error.getField(), error.getDefaultMessage()));
        }

        for (ObjectError error : ex.getBindingResult().getGlobalErrors()) {
            errors.add(new ExceptionErrorItem(error.getObjectName(), error.getDefaultMessage()));
        }

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }

    /**
     * To handle validation in controller method param level
     * HTTP Status: 422
     *
     * @see <a href="https://reflectoring.io/bean-validation-with-spring-boot/#returning-structured-error-responses">reflectoring.io - Returning Structured Error Responses</a>
     *
     * @param ex ConstraintViolationException
     * @param request Spring MVC Request
     *
     * @return Formatted Response Body
     */
    @ExceptionHandler({
        ConstraintViolationException.class, // Handling validation in controller level
    })
    public ResponseEntity<Object> handleConstraintValidationException(final ConstraintViolationException ex, final WebRequest request) {
        List<ExceptionErrorItem> errors = new ArrayList<>();

        for (ConstraintViolation violation : ex.getConstraintViolations()) {
            errors.add(new ExceptionErrorItem(violation.getPropertyPath().toString(), violation.getMessage()));
        }

        log.info("[Validation] Constraint Violation Exception [User = {}]", getLoggedInUser(request));
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage(), errors);
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }

    /**
     * to handle an Exception which is thrown on incorrect usage of the API, such as failing to
     * "compile" a query object that needed compilation before execution.
     *
     * HTTP Status: 422
     *
     * @param ex Default starter data not found exception
     * @param request Spring MVC Request
     * @return Formatted Response Body
     */
    @ExceptionHandler({
        InvalidDataAccessApiUsageException.class
    })
    public ResponseEntity<Object> handleInvalidDataAccessApiUsageException(final Exception ex, final WebRequest request) {
        log.info("Invalid Data Access Api UsageException [User = {}]", getLoggedInUser(request));
        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.UNPROCESSABLE_ENTITY, ex.getLocalizedMessage());
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }

    /**
     * Handle all of the exceptions that is not defined yet
     * HTTP Status: 500
     *
     * @param ex Default Exception
     * @param request Spring MVC Request
     *
     * @return Formatted Response Body
     */
    @ExceptionHandler({
        Exception.class
    })
    public ResponseEntity<Object> handleAll(Exception ex, WebRequest request) {
        log.error("INTERNAL SERVER ERROR [User = {}]", getLoggedInUser(request));

        ExceptionResponse exceptionResponse = new ExceptionResponse(HttpStatus.INTERNAL_SERVER_ERROR, ex.getLocalizedMessage(), "Oppps.. There is a something wrong, please comeback later.");
        return new ResponseEntity<>(exceptionResponse, new HttpHeaders(), exceptionResponse.getStatus());
    }

    private String getLoggedInUser(WebRequest request) {
        return request.getUserPrincipal() != null ? request.getUserPrincipal().getName() : "NOT LOGGED IN";
    }
}
