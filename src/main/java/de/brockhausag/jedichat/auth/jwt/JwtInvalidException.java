package de.brockhausag.jedichat.auth.jwt;

public class JwtInvalidException extends RuntimeException {
    public JwtInvalidException(String explanation) {
        super(explanation);
    }
}
