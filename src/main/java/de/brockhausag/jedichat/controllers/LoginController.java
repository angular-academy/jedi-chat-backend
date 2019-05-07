package de.brockhausag.jedichat.controllers;

import de.brockhausag.jedichat.auth.JediChatUserDetailsService;
import de.brockhausag.jedichat.auth.UserRole;
import de.brockhausag.jedichat.auth.jwt.JwtProviderService;
import de.brockhausag.jedichat.data.dto.CreateUserDto;
import de.brockhausag.jedichat.data.dto.JwtDto;
import de.brockhausag.jedichat.data.dto.LoginDto;
import de.brockhausag.jedichat.data.dto.UserDto;
import de.brockhausag.jedichat.data.entities.UserEntity;
import de.brockhausag.jedichat.exceptions.NickNameAlreadyExistsException;
import de.brockhausag.jedichat.exceptions.PasswordsNotMatchingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.*;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;
import static org.springframework.http.ResponseEntity.ok;

@RestController
@RequestMapping(value = "/", produces = { MediaTypes.HAL_JSON_VALUE })
public class LoginController {

    private final JediChatUserDetailsService userDetailsService;
    private final JwtProviderService jwtProviderService;

    @Autowired
    public LoginController(JediChatUserDetailsService userDetailsService, JwtProviderService jwtProviderService) {
        this.userDetailsService = userDetailsService;
        this.jwtProviderService = jwtProviderService;
    }

    @RequestMapping(value = "/register", method = RequestMethod.PUT)
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

    @PostMapping("/login")
    public ResponseEntity<JwtDto> login(@RequestBody LoginDto login) {
        try {
            String username = login.getNickname();
            String token = jwtProviderService.createToken(username);
            JwtDto dto = new JwtDto(token);
            return ok(dto);
        } catch (AuthenticationException e) {
            throw new BadCredentialsException("Invalid username/password supplied");
        }
    }
}
