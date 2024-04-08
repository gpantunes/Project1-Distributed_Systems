package tukano.impl.rest;

import tukano.api.Short;
import tukano.api.java.Shorts;
import tukano.api.rest.RestShorts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestShortsClass implements RestShorts, Serializable {

    Map<String, Short> shortMap = new HashMap<>();
    Shorts shortInterface = new ShortClass();

    @Override
    public Short createShort(String userId, String password) {

        //if(userServer.getUser(userdId, password) != null)
        return (Short) shortInterface.createShort(userId, password);
    }

    @Override
    public void deleteShort(String shortId, String password) {
        Short vid = shortMap.get(shortId);

    }

    @Override
    public Short getShort(String shortId) {
        return null;
    }

    @Override
    public List<String> getShorts(String userId) {
        return null;
    }

    @Override
    public void follow(String userId1, String userId2, boolean isFollowing, String password) {

    }

    @Override
    public List<String> followers(String userId, String password) {
        return null;
    }

    @Override
    public void like(String shortId, String userId, boolean isLiked, String password) {

    }

    @Override
    public List<String> likes(String shortId, String password) {
        return null;
    }

    @Override
    public List<String> getFeed(String userId, String password) {
        return null;
    }
}
