package id.co.bni.otf.bootcamp.web.exception;

/**
 * Default data duplication exception
 *
 * @author efriandika
 * @since 1.0.0
 */
public class DataNotUniqueException extends RuntimeException {
    public DataNotUniqueException(String message) {
        super(message);
    }

    public DataNotUniqueException(String message, Throwable cause) {
        super(message, cause);
    }
}
