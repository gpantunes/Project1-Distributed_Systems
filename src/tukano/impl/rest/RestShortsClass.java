package tukano.impl.rest;

import jakarta.ws.rs.WebApplicationException;
import jakarta.ws.rs.core.Response;
import tukano.api.Short;
import tukano.api.java.Shorts;
import tukano.api.rest.RestShorts;
import tukano.api.service.util.Result;
import tukano.impl.srv.java.JavaShorts;
import tukano.impl.srv.java.JavaUsers;


import java.io.Serializable;
import java.util.List;
import java.util.logging.Logger;

public class RestShortsClass implements RestShorts{

    Shorts shortServer = new JavaShorts();

    private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

    @Override
    public Short createShort(String userId, String password) {
        Log.info(String.format("REST create short " + userId));

        return checkResult(shortServer.createShort(userId, password));
    }

    @Override
    public void deleteShort(String shortId, String password) {
        Log.info(String.format("REST delete short " + shortId));

        checkResult(shortServer.deleteShort(shortId, password));
    }

    @Override
    public Short getShort(String shortId) {
        Log.info(String.format("REST get short " + shortId));

        return checkResult(shortServer.getShort(shortId));
    }

    @Override
    public List<String> getShorts(String userId) {
        Log.info(String.format("REST get shorts " + userId));

        return checkResult(shortServer.getShorts(userId));
    }

    @Override
    public void follow(String userId1, String userId2, boolean isFollowing, String password) {
        Log.info(String.format("REST follow " + userId1 + " " + userId2));

        checkResult(shortServer.follow(userId1, userId2, isFollowing, password));
    }

    @Override
    public List<String> followers(String userId, String password) {
        Log.info(String.format("REST followers " + userId));

        return checkResult(shortServer.followers(userId, password));
    }

    @Override
    public void like(String shortId, String userId, boolean isLiked, String password) {
        Log.info(String.format("REST like short " + shortId + " " + userId));

        checkResult(shortServer.like(shortId, userId, isLiked, password));
    }

    @Override
    public List<String> likes(String shortId, String password) {
        Log.info(String.format("REST likes " + shortId));

        return checkResult(shortServer.likes(shortId, password));
    }

    @Override
    public List<String> getFeed(String userId, String password) {
        Log.info(String.format("REST get feed " + userId));

        return checkResult(shortServer.getFeed(userId, password));
    }


    protected <T> T checkResult(Result<T> result){
        if(result.isOK())
            return result.value();
        else
            throw new WebApplicationException(convertStatus(result));
    }


    static protected Response.Status convertStatus(Result<?> result ) { //é possivel que haja casos a mais
        switch( result.error() ) {
            case CONFLICT:
                return Response.Status.CONFLICT;
            case NOT_FOUND:
                return Response.Status.NOT_FOUND;
            case FORBIDDEN:
                return Response.Status.FORBIDDEN;
            case BAD_REQUEST:
                return Response.Status.BAD_REQUEST;
            case NOT_IMPLEMENTED:
                return Response.Status.NOT_IMPLEMENTED;
            default:
                return Response.Status.INTERNAL_SERVER_ERROR;
        }
    }
}
