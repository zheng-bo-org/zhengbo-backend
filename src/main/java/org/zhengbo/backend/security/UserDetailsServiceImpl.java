package org.zhengbo.backend.security;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Component;
import org.zhengbo.backend.service.user.TokenService;
import org.zhengbo.backend.service.user.UserGeneralService;

@Component
@RequiredArgsConstructor
public class UserDetailsServiceImpl implements UserDetailsService {
    private final Logger log = LoggerFactory.getLogger(UserDetailsServiceImpl.class);
    private final UserGeneralService userGeneralService;
    private final TokenService tokenService;
    private final TokenService.UserRolesFinder userRolesFinder;


    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        var usernameAndUserType = tokenService.deCombineUsername(username);
        var targetUser = userGeneralService.findUserByUsername(usernameAndUserType.type(), usernameAndUserType.username());
        if (targetUser.isEmpty()) {
            log.error("No such user found. The username is: {}", username);
            throw new UsernameNotFoundException("No such user exists.");
        }else {
            var user = targetUser.get();
            return new TokenService.CustomUserDetails(
                    user.getId(),
                    user.getUsername(),
                    user.getPwd(),
                    user.getUserType(),
                    userRolesFinder
            );
        }
    }
}
