package de.brockhausag.jedichat.data.dto;

import de.brockhausag.jedichat.data.enums.Fraction;
import de.brockhausag.jedichat.data.enums.Gender;
import de.brockhausag.jedichat.data.enums.Species;
import lombok.Getter;
import lombok.Setter;

public class CreateUserDto {

    @Getter @Setter
    private String nickName;
    @Getter @Setter
    private String password;
    @Getter @Setter
    private String matchingPassword;
    @Getter @Setter
    private String avatar;
    @Getter @Setter
    private Gender gender;
    @Getter @Setter
    private Fraction fraction;
    @Getter @Setter
    private Species species;
    @Getter @Setter
    private String bio;
}
