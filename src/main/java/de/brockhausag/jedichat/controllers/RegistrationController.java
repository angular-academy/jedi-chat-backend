package de.brockhausag.jedichat.controllers;

import de.brockhausag.jedichat.auth.JediChatUserDetailsService;
import de.brockhausag.jedichat.auth.UserRole;
import de.brockhausag.jedichat.data.dto.CreateUserDto;
import de.brockhausag.jedichat.data.dto.UserDto;
import de.brockhausag.jedichat.data.entities.UserEntity;
import de.brockhausag.jedichat.exceptions.NickNameAlreadyExistsException;
import de.brockhausag.jedichat.exceptions.PasswordsNotMatchingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.net.URI;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@RestController
@RequestMapping(value = "/register", produces = { MediaTypes.HAL_JSON_VALUE })
public class RegistrationController {

    private final JediChatUserDetailsService userDetailsService;

    @Autowired
    public RegistrationController(JediChatUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    @RequestMapping(method = RequestMethod.PUT)
    public HttpEntity<URI> create(@RequestBody CreateUserDto userDto) {
        UserEntity created;
        try {
            created = userDetailsService.create(userDto, UserRole.USER);
        } catch (PasswordsNotMatchingException | NickNameAlreadyExistsException e) {
            return ResponseEntity.badRequest().build();
        }
        UserDto dto = UserDto.FromEntity(created);
        URI location = linkTo(methodOn(UserController.class).getUser(dto.getNickName())).toUri();
        return ResponseEntity.created(location).body(location);
    }
}
