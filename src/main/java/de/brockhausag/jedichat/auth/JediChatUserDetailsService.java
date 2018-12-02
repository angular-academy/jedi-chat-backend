package de.brockhausag.jedichat.auth;

import de.brockhausag.jedichat.data.dto.CreateUserDto;
import de.brockhausag.jedichat.data.dto.UserDto;
import de.brockhausag.jedichat.data.entities.UserEntity;
import de.brockhausag.jedichat.exceptions.PasswordsNotMatchingException;
import de.brockhausag.jedichat.repositories.UserRepository;
import de.brockhausag.jedichat.util.DecodingUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.persistence.EntityNotFoundException;
import java.util.Optional;

@Service
public class JediChatUserDetailsService implements UserDetailsService {

     private final UserRepository userRepository;

    @Autowired
    public JediChatUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        UserEntity userEntity = userRepository.findByNickName(username);

        if (userEntity == null) {
            throw  new UsernameNotFoundException(username);
        } else {
            return new JediChatUserPrincipal(userEntity);
        }
    }

    public JediChatUserPrincipal loadById(Long id) throws EntityNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        if (!optionalUserEntity.isPresent()) {
            throw new EntityNotFoundException(String.format("User with id %d does not exist.", id));
        } else {
            return new JediChatUserPrincipal(optionalUserEntity.get());
        }
    }

    public UserEntity create(CreateUserDto userDto, UserRole role) throws PasswordsNotMatchingException {
        if (!userDto.getPassword().equals(userDto.getMatchingPassword())) {
            throw new PasswordsNotMatchingException("Passwords not matching. Not creating user.");
        }
        UserEntity entity = new UserEntity();
        entity.setNickName(userDto.getNickName());
        entity.setPasswordHash(new BCryptPasswordEncoder().encode(userDto.getPassword()));
        entity.setRole(role);
        entity.setAvatar(DecodingUtil.base64ToByeArray(userDto.getAvatar()));
        entity.setBio(userDto.getBio());
        entity.setFraction(userDto.getFraction());
        entity.setGender(userDto.getGender());
        entity.setSpecies(userDto.getSpecies());
        return userRepository.save(entity);
    }

    public UserDto getCurrentUser() {
        return getPrincipal().getUserDto();
    }

    public UserEntity getCurrentUserEntity() {
        return getPrincipal().getUserEntity();
    }

    private JediChatUserPrincipal getPrincipal() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String userName = authentication.getName();
        return (JediChatUserPrincipal) loadUserByUsername(userName);
    }
}
