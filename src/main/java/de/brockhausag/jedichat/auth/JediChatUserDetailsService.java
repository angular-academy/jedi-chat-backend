package de.brockhausag.jedichat.auth;

import de.brockhausag.jedichat.data.dto.CreateUserDto;
import de.brockhausag.jedichat.data.dto.UserDto;
import de.brockhausag.jedichat.data.entities.UserEntity;
import de.brockhausag.jedichat.exceptions.NickNameAlreadyExistsException;
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
    public UserDetails loadUserByUsername(String nickName) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByNickName(nickName);
        return optionalUserEntity
                .map(JediChatUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException(nickName));
    }

    public JediChatUserPrincipal loadById(Long id) throws EntityNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findById(id);
        return optionalUserEntity
                .map(JediChatUserPrincipal::new)
                .orElseThrow(() -> new EntityNotFoundException(String.format("User with id %d does not exist.", id)));
    }

    public JediChatUserPrincipal loadByNickName(String nickName) throws UsernameNotFoundException {
        Optional<UserEntity> optionalUserEntity = userRepository.findByNickName(nickName);
        return optionalUserEntity
                .map(JediChatUserPrincipal::new)
                .orElseThrow(() -> new UsernameNotFoundException(nickName));
    }

    public UserEntity create(CreateUserDto userDto, UserRole role) throws PasswordsNotMatchingException, NickNameAlreadyExistsException {
        if (!userDto.getPassword().equals(userDto.getMatchingPassword())) {
            throw new PasswordsNotMatchingException("Passwords not matching. Not creating user.");
        }
        if (userRepository.existsByNickName(userDto.getNickName())) {
            throw new NickNameAlreadyExistsException(String.format("NickName %s already exists. Not creating user.", userDto.getNickName()));
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
}
