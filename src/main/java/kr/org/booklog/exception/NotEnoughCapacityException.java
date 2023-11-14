package kr.org.booklog.exception;

public class NotEnoughCapacityException extends RuntimeException {

    public NotEnoughCapacityException() {
        super();
    }

    public NotEnoughCapacityException(String message) {
        super(message);
    }

    public NotEnoughCapacityException(String message, Throwable cause) {
        super(message, cause);
    }

    public NotEnoughCapacityException(Throwable cause) {
        super(cause);
    }
}