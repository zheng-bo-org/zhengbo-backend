package org.zhengbo.backend.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zhengbo.backend.model.user.Role;
import org.zhengbo.backend.repository.RoleRepository;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SimpleUserRoleService implements UserRoleService{
    private final RoleRepository roleRepository;

    @Override
    public List<Role> findRoles(Long userId) {
        return new ArrayList<>();
    }
}
