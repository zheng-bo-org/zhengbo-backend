package org.zhengbo.backend.service.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.zhengbo.backend.model.user.TypeOfUser;
import org.zhengbo.backend.model.user.User;
import org.zhengbo.backend.repository.UserRepository;

import java.util.Optional;


@Service
@RequiredArgsConstructor
public class SimpleUserGeneralService implements UserGeneralService {
    private final UserRepository userRepository;

    @Override
    public Optional<User> findUserByUsername(TypeOfUser type, String username) {
        return userRepository.findFirstByUserTypeAndUsername(type, username);
    }
}