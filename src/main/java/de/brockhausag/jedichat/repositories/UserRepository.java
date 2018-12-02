package de.brockhausag.jedichat.repositories;


import de.brockhausag.jedichat.data.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    Optional<UserEntity> findByNickName(String nickName);
    boolean existsByNickName(String nickName);
}
