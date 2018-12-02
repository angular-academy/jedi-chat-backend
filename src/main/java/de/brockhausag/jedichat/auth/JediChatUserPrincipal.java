package de.brockhausag.jedichat.auth;

import de.brockhausag.jedichat.data.dto.UserDto;
import de.brockhausag.jedichat.data.entities.UserEntity;
import lombok.Getter;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public class JediChatUserPrincipal implements UserDetails {

    @Getter
    private UserEntity userEntity;

    public JediChatUserPrincipal(UserEntity userEntity) {
        this.userEntity = userEntity;
    }

    public UserDto getUserDto() {
        return UserDto.FromEntity(userEntity);
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        List<UserRole> roles = new ArrayList<>();
        roles.add(UserRole.USER);

        if (userEntity.getRole() == UserRole.ADMIN) {
            roles.add(UserRole.ADMIN);
        }

        return roles.stream()
                .map(userRole -> new SimpleGrantedAuthority(userRole.toString()))
                .collect(Collectors.toList());
    }

    @Override
    public String getPassword() {
        return userEntity.getPasswordHash();
    }

    @Override
    public String getUsername() {
        return userEntity.getNickName();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
