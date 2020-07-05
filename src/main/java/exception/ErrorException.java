package exception;

public class ErrorException extends RuntimeException {

    public ErrorException(String body, Exception exception) {
        super(body, exception);
    }
}
