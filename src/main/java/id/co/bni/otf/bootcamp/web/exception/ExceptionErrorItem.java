package id.co.bni.otf.bootcamp.web.exception;


/**
 * Default exception response errors item
 *
 * @since 1.0.0
 * @author efriandika
 */
public class ExceptionErrorItem {
    private String field;
    private String message;

    public ExceptionErrorItem(String field, String message) {
        this.field = field;
        this.message = message;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
