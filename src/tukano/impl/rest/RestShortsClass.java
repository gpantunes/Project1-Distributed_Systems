package tukano.impl.rest;

import tukano.api.Short;
import tukano.api.java.Shorts;
import tukano.api.rest.RestShorts;
import tukano.impl.srv.common.JavaShorts;

import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class RestShortsClass implements RestShorts, Serializable {

    Shorts shortServer = new JavaShorts();

    @Override
    public Short createShort(String userId, String password) {
        return shortServer.createShort(userId, password).value();
    }

    @Override
    public void deleteShort(String shortId, String password) {
        shortServer.deleteShort(shortId, password);
    }

    @Override
    public Short getShort(String shortId) {
        return shortServer.getShort(shortId).value();
    }

    @Override
    public List<String> getShorts(String userId) {
        return shortServer.getShorts(userId).value();
    }

    @Override
    public void follow(String userId1, String userId2, boolean isFollowing, String password) {
        shortServer.follow(userId1, userId2, isFollowing, password);
    }

    @Override
    public List<String> followers(String userId, String password) {
        return shortServer.followers(userId, password).value();
    }

    @Override
    public void like(String shortId, String userId, boolean isLiked, String password) {
        shortServer.like(shortId, userId, isLiked, password);
    }

    @Override
    public List<String> likes(String shortId, String password) {
        return shortServer.likes(shortId, password).value();
    }

    @Override
    public List<String> getFeed(String userId, String password) {
        return shortServer.getFeed(userId, password).value();
    }
}
