package de.brockhausag.jedichat.data.dto;


import de.brockhausag.jedichat.auth.UserRole;
import de.brockhausag.jedichat.controllers.UserController;
import de.brockhausag.jedichat.data.entities.UserEntity;
import de.brockhausag.jedichat.data.enums.Fraction;
import de.brockhausag.jedichat.data.enums.Gender;
import de.brockhausag.jedichat.data.enums.Species;
import de.brockhausag.jedichat.util.DecodingUtil;
import lombok.Getter;
import lombok.Setter;
import org.springframework.hateoas.ResourceSupport;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

public class UserDto extends ResourceSupport {

    @Getter @Setter
    private long userId;
    @Getter @Setter
    private String nickName;
    @Getter @Setter
    private UserRole role;
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

    public static UserDto FromEntity(UserEntity entity) {
        UserDto result = new UserDto();
        result.setUserId(entity.getId());
        result.setNickName(entity.getNickName());
        result.setRole(entity.getRole());
        result.setAvatar(DecodingUtil.byteArrayToBase64(entity.getAvatar()));
        result.setGender(entity.getGender());
        result.setFraction(entity.getFraction());
        result.setSpecies(entity.getSpecies());
        result.setBio(entity.getBio());
        result.add(linkTo(methodOn(UserController.class).getUser(entity.getId())).withSelfRel());
        return result;
    }
}
