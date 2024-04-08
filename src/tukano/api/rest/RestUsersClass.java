package tukano.api.rest;

import tukano.api.User;
import tukano.api.java.Users;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestUsersClass implements RestUsers{

    Map<String, User> userMap = new HashMap<>();
    Users userInterface = new UsersClass();

    @Override
    public String createUser(User user) {

        String userID = userInterface.createUser(user).value();
        userMap.put(userID, user);

        return userID;
    }

    @Override
    public User getUser(String userId, String pwd) {
        User user = userMap.get(userId);

        if(user.getPwd().equals(pwd))
            return user;

        return null;
    }

    @Override
    public User updateUser(String userId, String pwd, User user) {
        return null;
    }

    @Override
    public User deleteUser(String userId, String pwd) {
        User user = userMap.get(userId);

        if(user.getPwd().equals(pwd))
            return userMap.remove(userId);

        return null;
    }

    @Override
    public List<User> searchUsers(String pattern) {
        

        return null;
    }
}
