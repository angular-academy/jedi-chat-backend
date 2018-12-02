package de.brockhausag.jedichat.repositories;


import de.brockhausag.jedichat.data.entities.UserEntity;
import org.springframework.data.repository.CrudRepository;

public interface UserRepository extends CrudRepository<UserEntity, Long> {
    UserEntity findByNickName(String nickName);
}
