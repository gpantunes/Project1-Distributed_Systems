package tukano.impl.srv.java;

import static tukano.api.service.util.Result.ErrorCode.*;
import static tukano.api.service.util.Result.error;
import static tukano.api.service.util.Result.ok;

import java.net.URI;
import java.util.*;
import java.util.Comparator;

import java.util.logging.Logger;

import tukano.api.Follow;
import tukano.api.Likes;
import tukano.api.Short;
import tukano.api.java.Users;
import tukano.api.service.util.Result;
import tukano.impl.Hibernate;
import tukano.impl.client.UsersClientFactory;
import tukano.impl.discovery.Discovery;

public class JavaShorts implements tukano.api.java.Shorts {
    private static Logger Log = Logger.getLogger(JavaShorts.class.getName());

    Discovery discovery = Discovery.getInstance();
    URI[] blobUris = discovery.findUrisOf("blobs", -1);
    Users client = UsersClientFactory.getClient();

    @Override
    public Result<Short> createShort(String userId, String password) {
        var result = client.getUser(userId, password);

        if(!result.isOK()) {
            Log.info(String.valueOf(error(result.error())));
            return Result.error(result.error());
        }

        try {
            discovery.addBlobUris(blobUris);
            URI nextUri = discovery.getNextServer();

            String shortId = String.valueOf(UUID.randomUUID());
            String blobId =  nextUri + "/blobs/" + shortId;

            Short vid = new Short(shortId, userId, blobId, System.currentTimeMillis(), 0);

            discovery.updateBlobDistribution(nextUri);

            Hibernate.getInstance().persist(vid);

            return ok(vid);
        }catch (Exception e){
            return Result.error(INTERNAL_ERROR);
        }
    }

    @Override
    public Result<Void> deleteShort(String shortId, String password) {
        if(badParam(shortId) || badParam(password))
            return error(BAD_REQUEST);

        Short vid = getShort(shortId).value();
        if(vid == null)
            return error(NOT_FOUND);

        String ownerId = vid.getOwnerId();

        var result = client.getUser(ownerId, password);
        if(!result.isOK())
            return Result.error(result.error());


        Hibernate.getInstance().delete(vid);

        return ok();
    }

    @Override
    public Result<Short> getShort(String shortId) {
        if(badParam(shortId))
            return error(BAD_REQUEST);

        var shortList = Hibernate.getInstance().sql("SELECT * FROM Short WHERE shortId = '" + shortId + "'", Short.class);

        if(shortList.isEmpty())
            return error(NOT_FOUND);

        return ok(shortList.get(0));
    }

    @Override
    public Result<List<String>> getShorts(String userId) {
        if(badParam(userId))
            return error(BAD_REQUEST);

       var shortList = Hibernate.getInstance().sql("SELECT * FROM Short WHERE ownerId = '" + userId + "'", Short.class);
       List<String> idList = new ArrayList<>(shortList.size());

        for(int i = 0; i < shortList.size(); i++)
            idList.add(i, shortList.get(i).getShortId());

        return ok(idList);
    }

    @Override
    public Result<Void> follow(String userId1, String userId2, boolean isFollowing, String password) {
        if(badParam(userId1) || badParam(userId2) || badParam(password))
            return error(BAD_REQUEST);

        var result1 = client.getUser(userId1, password);
        if(!result1.isOK())
            return Result.error(result1.error());


        var follow = Hibernate.getInstance().sql("SELECT * FROM Follow WHERE followerId = '"
                + userId1  + "' AND followedId = '" + userId2 + "'", Follow.class);

        if(isFollowing){
            if(follow.isEmpty())
                Hibernate.getInstance().persist(new Follow(userId1, userId2));
            else return error(CONFLICT);
        } else {
            if(!follow.isEmpty())
                Hibernate.getInstance().delete(follow.get(0));
        }

        return ok();
    }

    @Override
    public Result<List<String>> followers(String userId, String password) {
        if(badParam(userId) || badParam(password))
            return error(BAD_REQUEST);

        var result = client.getUser(userId, password);

        if(!result.isOK())
            return Result.error(result.error());

        var followerList = Hibernate.getInstance().sql("SELECT * FROM Follow WHERE followedId = '"
                + userId + "'", Follow.class);

        List<String> idList = new ArrayList<>(followerList.size());

        for(int i = 0; i < followerList.size(); i++)
            idList.add(i, followerList.get(i).getFollowerId());

        return ok(idList);
    }

    @Override
    public Result<Void> like(String shortId, String userId, boolean isLiked, String password) {
        if(badParam(shortId) || badParam(userId) || badParam(password))
            return error(BAD_REQUEST);

        var result = client.getUser(userId, password);
        var vid = getShort(shortId).value();
        int totalLikes = vid.getTotalLikes();

        if(!result.isOK())
            return Result.error(result.error());


        var likeList = Hibernate.getInstance().sql("SELECT * FROM Likes WHERE userId = '"
                + userId + "' AND shortId = '" + shortId + "'" , Likes.class);

        if(isLiked){
            if(!likeList.isEmpty())
                return error(CONFLICT);

            /*var userLikes = Hibernate.getInstance().sql("SELECT * FROM Likes WHERE userId = '"
                    + userId + "'", Likes.class);*/

            Hibernate.getInstance().persist(new Likes(userId, shortId));
            vid.setTotalLikes(totalLikes + 1);
            Hibernate.getInstance().update(vid);
        }else {
            if(likeList.isEmpty())
                return error(NOT_FOUND);

            Hibernate.getInstance().delete(likeList.get(0));
            vid.setTotalLikes(totalLikes - 1);
            Hibernate.getInstance().update(vid);
        }
        return ok();
    }

    @Override
    public Result<List<String>> likes(String shortId, String password) {
        if(badParam(shortId) || badParam(password))
            return error(BAD_REQUEST);

        Short vid = getShort(shortId).value();
        if(vid == null)
            return error(NOT_FOUND);

        String ownerId = vid.getOwnerId();
        var result = client.getUser(ownerId, password);
        if(!result.isOK())
            return Result.error(result.error());


        var likeList = Hibernate.getInstance().sql("SELECT * FROM Likes WHERE shortId = '"
                + shortId + "'", Likes.class);

        List<String> likeIdList = new ArrayList<>();
        for(int i = 0; i < likeList.size(); i++){
            likeIdList.add(likeList.get(i).getUserId());
        }

        return ok(likeIdList);
    }

    @Override
    public Result<List<String>> getFeed(String userId, String password) {
        var result = client.getUser(userId, password);

        if(!result.isOK()) {
            return Result.error(result.error());
        }

        var followList = Hibernate.getInstance().sql("SELECT * FROM Follow WHERE followerId = '"
                + userId + "'", Follow.class);

        List<String> followedIdList = new ArrayList<>(followList.size());
        for(int i = 0; i < followList.size(); i++)
            followedIdList.add(i, followList.get(i).getFollowedId());

        followedIdList.add(userId);

        List<Short> completeShortList = new ArrayList<>();

        for(int i = 0; i < followedIdList.size(); i++){
            var shortList = Hibernate.getInstance().sql("SELECT * FROM Short WHERE ownerId = '"
                    + followedIdList.get(i) + "'", Short.class);

            completeShortList.addAll(shortList);
        }

        Collections.sort(completeShortList, new ShortTimestampComparator());

        List<String> shortIdList = new ArrayList<>();
        for(int k = 0; k < completeShortList.size(); k++){
            shortIdList.add(k, completeShortList.get(k).getShortId());
        }

        return ok(shortIdList);

    }

    public class ShortTimestampComparator implements Comparator<Short> {
        @Override
        public int compare(Short s1, Short s2) {
            return Long.compare(s2.getTimestamp(), s1.getTimestamp());
        }
    }



    private boolean badParam(String str) {
        return str == null;
    }
}
