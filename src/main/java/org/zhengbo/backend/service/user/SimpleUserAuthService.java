package org.zhengbo.backend.service.user;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Isolation;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.zhengbo.backend.dml.Dml;
import org.zhengbo.backend.global_exceptions.user.UserAuthException;
import org.zhengbo.backend.model.user.TypeOfUser;
import org.zhengbo.backend.model.user.User;
import org.zhengbo.backend.repository.UserRepository;

import java.util.Optional;


@Service
public class SimpleUserAuthService implements UserAuthService {
    private final Logger log = LoggerFactory.getLogger(SimpleUserAuthService.class);
    private final PasswordEncoder passwordEncoder;
    private final Dml dml;
    private final TokenService tokenService;
    private final TokenService.UserRolesFinder userRolesFinder;
    private final UserRepository userRepository;

    SimpleUserAuthService(PasswordEncoder passwordEncoder, Dml dml, TokenService tokenService, TokenService.UserRolesFinder userRolesFinder, UserRepository userRepository) {
        this.passwordEncoder = passwordEncoder;
        this.dml = dml;
        this.tokenService = tokenService;

        this.userRolesFinder = userRolesFinder;
        this.userRepository = userRepository;
    }


    @FunctionalInterface
    interface SignUpChecker {
        void check(String username, TypeOfUser type, UserRepository userRepo) throws UserAuthException;
    }

    record UserNameLock() implements Dml.LockType {

    }

    private final SignUpChecker usernameShouldValid = (username, type, userRepo) -> {
        if (username.length() < 4) {
            throw new UserAuthException(UserAuthException.UserAuthExceptionCode.USERNAME_TOO_SHORT, HttpStatus.BAD_REQUEST);
        }


        Optional<User> targetUser = userRepo.findFirstByUserTypeAndUsername(type, username);
        if (targetUser.isPresent()) {
            throw new UserAuthException(UserAuthException.UserAuthExceptionCode.USERNAME_DUPLICATED, HttpStatus.BAD_REQUEST);
        }
    };


    @Transactional(isolation = Isolation.READ_COMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public String signIn(String username, String pwd, TypeOfUser type) {
        log.info("Sign in for the user. Username {} type {}", username, type);
        Optional<User> targetUser = userRepository.findFirstByUserTypeAndUsername(type, username);
        if (targetUser.isEmpty()) {
            throw new UserAuthException(UserAuthException.UserAuthExceptionCode.USER_NOT_FOUND, HttpStatus.NOT_FOUND);
        }

        var user = targetUser.get();
        boolean pwdMatched = passwordEncoder.matches(pwd, user.getPwd());
        if (!pwdMatched) {
            throw new UserAuthException(UserAuthException.UserAuthExceptionCode.INCORRECT_PASSWORD, HttpStatus.BAD_REQUEST);
        }

        return tokenService.signToken(new TokenService.CustomUserDetails(user.getId(), user.getUsername(), user.getPwd(), user.getUserType(), userRolesFinder));
    }

    @Override
    public void signOut(Long userId) {
        tokenService.makeCurrentTokenInvalid();
    }

    @Transactional(isolation = Isolation.READ_UNCOMMITTED, propagation = Propagation.REQUIRED)
    @Override
    public String signUp(String username, String pwd, TypeOfUser type) {
        log.info("Sign up account.  username {} type {}", username, type);
        try {
            dml.lock(username + type, UserNameLock.class);
            usernameShouldValid.check(username, type, userRepository);
            User user = new User();
            user.setUserType(type);
            user.setPwd(passwordEncoder.encode(pwd));
            user.setUsername(username);
            var saved = userRepository.save(user);
            log.info("Sign up success. username: {} id {}", saved.getUsername(), saved.getId());
            return tokenService.signToken(new TokenService.CustomUserDetails(saved.getId(), saved.getUsername(), saved.getPwd(), saved.getUserType(), userRolesFinder));
        } finally {
            dml.unlock(username + type, UserNameLock.class);
        }
    }
}
