package org.zhengbo.backend.service.user;

import org.zhengbo.backend.model.user.TypeOfUser;
import org.zhengbo.backend.model.user.User;

import javax.swing.text.html.Option;
import java.util.Optional;

public interface UserGeneralService  {
    Optional<User> findUserByUsername(TypeOfUser type, String username);

    User findUserById(Long userId);
}