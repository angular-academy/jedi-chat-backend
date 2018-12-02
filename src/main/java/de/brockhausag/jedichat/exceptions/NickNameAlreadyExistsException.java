package de.brockhausag.jedichat.exceptions;

public class NickNameAlreadyExistsException extends Exception {
    public NickNameAlreadyExistsException(String message) {
        super(message);
    }
}
