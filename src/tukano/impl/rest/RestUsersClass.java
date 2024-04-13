package tukano.impl.rest;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response.Status;
import tukano.api.User;
import tukano.api.java.Users;
import tukano.api.rest.RestUsers;
import tukano.api.service.util.Result;
import tukano.impl.srv.common.JavaUsers;

import java.util.List;
import java.util.logging.Logger;


public class RestUsersClass implements RestUsers {

    Users userServer = new JavaUsers();

    private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

    @Override
    public String createUser(User user) {
        Log.info(String.format("REST create user " + user.getUserId()));

        return checkResult(userServer.createUser(user));
    }

    @Override
    public User getUser(String userId, String pwd) {
        Log.info(String.format("REST get user " + userId));

        return checkResult(userServer.getUser(userId, pwd));
    }

    @Override
    public User updateUser(String userId, String pwd, User user) {
        Log.info(String.format("REST update user " + userId));

        return checkResult(userServer.updateUser(userId, pwd, user));
    }

    @Override
    public User deleteUser(String userId, String pwd) {
        Log.info(String.format("REST delete user " + userId));

        return checkResult(userServer.deleteUser(userId, pwd));
    }

    @Override
    public List<User> searchUsers(String pattern) {
        Log.info(String.format("REST search users " + pattern));

        return checkResult(userServer.searchUsers(pattern));
    }



    protected <T> T checkResult(Result<T> result){
        if(result.isOK())
            return result.value();
        else
            throw new WebApplicationException(convertStatus(result));
    }


    static protected Status convertStatus( Result<?> result ) { //é possivel que haja casos a mais
        switch( result.error() ) {
            case CONFLICT:
                return Status.CONFLICT;
            case NOT_FOUND:
                return Status.NOT_FOUND;
            case FORBIDDEN:
                return Status.FORBIDDEN;
            case BAD_REQUEST:
                return Status.BAD_REQUEST;
            case NOT_IMPLEMENTED:
                return Status.NOT_IMPLEMENTED;
            default:
                return Status.INTERNAL_SERVER_ERROR;
        }
    }
}
