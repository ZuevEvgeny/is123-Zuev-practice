package exceptions;

public class BeanTypeMismatchException extends RuntimeException {
    public BeanTypeMismatchException(String message) {
        super(message);
    }
}