package org.zhengbo.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.zhengbo.backend.model.user.UserRole;

public interface UserRoleRepository extends CrudRepository<UserRole, Long> {
}
