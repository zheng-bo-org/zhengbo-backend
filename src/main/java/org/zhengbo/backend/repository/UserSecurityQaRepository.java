package org.zhengbo.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.zhengbo.backend.model.user.User;
import org.zhengbo.backend.model.user.UserSecurityQa;

import java.util.List;

public interface UserSecurityQaRepository extends CrudRepository<UserSecurityQa, Long> {
    void deleteAllByUser(User user);

    List<UserSecurityQa> findAllByUser(User user);
}
