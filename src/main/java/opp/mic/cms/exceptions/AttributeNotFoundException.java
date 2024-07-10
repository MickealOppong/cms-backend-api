package opp.mic.cms.exceptions;

public class AttributeNotFoundException extends RuntimeException{
    public AttributeNotFoundException(String message) {
        super(message);
    }

    public AttributeNotFoundException(String message, Throwable cause) {
        super(message, cause);
    }
}
