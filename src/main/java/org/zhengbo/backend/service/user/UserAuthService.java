package org.zhengbo.backend.service.user;

import org.zhengbo.backend.model.user.TypeOfUser;

public interface UserAuthService {
    /**
     * sign in
     * @return token
     */
    String signIn(String username, String pwd, TypeOfUser type);

    /**
     * log out
     * @param userId which user should be sign ou.
     */
    void signOut(Long userId);

    /**
     * do sign up for the user and then return the token.
     * @param username username
     * @param pwd pwd
     * @param type type of user.
     * @return token
     */
    String signUp(String username, String pwd, TypeOfUser type);
}
