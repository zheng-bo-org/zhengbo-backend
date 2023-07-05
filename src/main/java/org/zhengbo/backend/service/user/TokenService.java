package org.zhengbo.backend.service.user;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.zhengbo.backend.model.user.Role;
import org.zhengbo.backend.model.user.TypeOfUser;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

public interface TokenService {
    interface UserRolesFinder {
        List<Role> findRoles(Long userId);
    }

    record CustomUserDetails(Long userId, String username, String pwd, TypeOfUser userType, UserRolesFinder userRolesFinder) implements org.springframework.security.core.userdetails.UserDetails {

        @Override
        public Collection<? extends GrantedAuthority> getAuthorities() {
            var roles = userRolesFinder.findRoles(userId);
            return roles
                    .stream()
                    .map(permission -> new SimpleGrantedAuthority("ROLE_" + permission.getRole()))
                    .collect(Collectors.toList());
        }

        @Override
        public String getPassword() {
            return pwd;
        }

        @Override
        public String getUsername() {
            return username;
        }

        @Override
        public boolean isAccountNonExpired() {
            return true;
        }

        @Override
        public boolean isAccountNonLocked() {
            return true;
        }

        @Override
        public boolean isCredentialsNonExpired() {
            return true;
        }

        @Override
        public boolean isEnabled() {
            return true;
        }
    }

    boolean isAbleToCreateNewTokenForTheUser(Long userId);

    String signToken(CustomUserDetails customUserDetails);

    boolean isTokenValid(String token, CustomUserDetails customUserDetails);

    void makeTheTokenInvalid(String token);

    void makeCurrentTokenInvalid();

    CustomUserDetails tokenToUserDetails(String token);

    record CombinedUsername(Long userId,String username, TypeOfUser type) {

    }

    CombinedUsername deCombineUsername(String username);

    String combineUsername(CombinedUsername username);

    record SimpleUserDetails(Long userId) {

    }

    Long getCurrentUser();

    String refreshTokenForCurrentUser();
}
