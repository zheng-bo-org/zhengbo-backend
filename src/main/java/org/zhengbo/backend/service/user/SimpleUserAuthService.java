package org.zhengbo.backend.service.user;

import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.zhengbo.backend.model.user.TypeOfUser;
import org.zhengbo.backend.repository.UserRepository;


@Service
@RequiredArgsConstructor
public class SimpleUserAuthService implements UserAuthService{
    private final Logger log = LoggerFactory.getLogger(SimpleUserAuthService.class);
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    interface SignInChecker {

    }

    @Override
    public String signIn(String username, String pwd, TypeOfUser type) {
        log.info("Sign in for the user. Username {} type {}", username, type);
        return null;
    }

    @Override
    public void signOut(Long userId) {

    }

    @Override
    public String signUp(String username, String pwd, TypeOfUser type) {
        return null;
    }
}
