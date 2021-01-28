package id.co.bni.otf.bootcamp.web.exception;

/**
 * Default data not found exception
 *
 * @author efriandika
 * @since 1.0.0
 */
public class DataNotFoundException extends RuntimeException {
    public DataNotFoundException(String message) {
        super(message);
    }

    public DataNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
