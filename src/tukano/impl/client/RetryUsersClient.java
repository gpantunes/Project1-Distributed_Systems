package tukano.impl.client;

import tukano.api.User;
import tukano.api.java.Users;
import tukano.api.service.util.Result;

import java.util.List;

public class RetryUsersClient extends RetryClient implements Users {

    final Users impl;

    public RetryUsersClient( Users impl ) {
        this.impl = impl;
    }

    @Override
    public Result<String> createUser(User user) {
        return reTry( () -> impl.createUser(user));
    }

    @Override
    public Result<User> getUser(String userId, String password) {
        return reTry( () -> impl.getUser(userId, password));
    }


    @Override
    public Result<User> updateUser(String userId, String pwd, User user) {
        return null;
    }

    @Override
    public Result<User> deleteUser(String userId, String pwd) {
        return null;
    }

    @Override
    public Result<List<User>> searchUsers(String pattern) {
        return null;
    }
}