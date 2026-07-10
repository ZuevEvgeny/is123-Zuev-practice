package exceptions;

public class NonUniqueBeanException extends RuntimeException {
    public NonUniqueBeanException(String message) {
        super(message);
    }
}