package org.zhengbo.backend.repository;

import org.springframework.data.repository.CrudRepository;
import org.zhengbo.backend.model.user.Role;

public interface RoleRepository extends CrudRepository<Role, Long> {
}
