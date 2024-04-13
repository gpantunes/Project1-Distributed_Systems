package tukano.impl.rest;

import tukano.api.User;
import tukano.api.java.Users;
import tukano.api.rest.RestUsers;
import tukano.impl.srv.common.JavaUsers;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class RestUsersClass implements RestUsers {

    Users userServer = new JavaUsers();

    @Override
    public String createUser(User user) {
        return userServer.createUser(user).value();
    }

    @Override
    public User getUser(String userId, String pwd) {
        return userServer.getUser(userId, pwd).value();
    }

    @Override
    public User updateUser(String userId, String pwd, User user) {
        return userServer.updateUser(userId, pwd, user).value();
    }

    @Override
    public User deleteUser(String userId, String pwd) {
        return userServer.deleteUser(userId, pwd).value();
    }

    @Override
    public List<User> searchUsers(String pattern) {
        return userServer.searchUsers(pattern).value();
    }
}
