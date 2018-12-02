package de.brockhausag.jedichat.controllers;

import de.brockhausag.jedichat.auth.JediChatUserDetailsService;
import de.brockhausag.jedichat.auth.JediChatUserPrincipal;
import de.brockhausag.jedichat.data.dto.UserDto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.MediaTypes;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import javax.persistence.EntityNotFoundException;

@RestController
@RequestMapping(value = "/api/user", produces = {MediaTypes.HAL_JSON_VALUE})
public class UserController {
    private static final String USER_ID = "userId";

    private final
    JediChatUserDetailsService userDetailsService;

    @Autowired
    public UserController(JediChatUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    //TODO: method argument provider or resolver?, passes user to method
    @RequestMapping(method = RequestMethod.GET)
    public HttpEntity<UserDto> user() {
        UserDto result = userDetailsService.getCurrentUser();
        return ResponseEntity.ok(result);
    }

    @RequestMapping(value = "/{" + USER_ID + "}", method = RequestMethod.GET)
    public HttpEntity<UserDto> getUser(@PathVariable(USER_ID) Long id) {
        JediChatUserPrincipal principal;
        try {
             principal = userDetailsService.loadById(id);
        } catch (EntityNotFoundException e) {
            return ResponseEntity.notFound().build();
        }
        UserDto result  = principal.getUserDto();
        return ResponseEntity.ok(result);
    }
}
