package de.brockhausag.jedichat.data.dto;

import lombok.Data;

@Data
public class LoginDto {
    private String nickname;
    private String password;
}
