package opp.mic.cms.exceptions;

public class PhotoStorageException extends RuntimeException{
    public PhotoStorageException(String message) {
        super(message);
    }

    public PhotoStorageException(String message, Throwable cause) {
        super(message, cause);
    }
}
