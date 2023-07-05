package org.zhengbo.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;
import org.zhengbo.backend.model.user.TypeOfUser;
import org.zhengbo.backend.model.user.User;

import java.util.Optional;

public interface UserRepository extends CrudRepository<User, Long> {
    Optional<User> findFirstByUserTypeAndUsername(TypeOfUser type, String username);
//    Optional<User> findFirstByUserTypeAndUsername(TypeOfUser type,String username);
}
