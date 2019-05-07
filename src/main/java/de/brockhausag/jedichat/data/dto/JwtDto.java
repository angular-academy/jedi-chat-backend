package de.brockhausag.jedichat.data.dto;

import lombok.Data;

@Data
public class JwtDto {
    private String token;

    public JwtDto() {
    }

    public JwtDto(String token) {
        this.token = token;
    }
}
