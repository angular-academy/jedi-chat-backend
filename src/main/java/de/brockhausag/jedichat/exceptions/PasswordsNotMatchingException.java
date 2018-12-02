package de.brockhausag.jedichat.exceptions;

public class PasswordsNotMatchingException extends Exception {
    public PasswordsNotMatchingException() {
        super();
    }

    public PasswordsNotMatchingException(String message) {
        super(message);
    }

    public PasswordsNotMatchingException(String message, Throwable cause) {
        super(message, cause);
    }
}
