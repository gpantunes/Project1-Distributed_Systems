package tukano.impl.srv.java;

import static tukano.api.service.util.Result.ErrorCode.*;
import static tukano.api.service.util.Result.error;
import static tukano.api.service.util.Result.ok;

import java.util.ArrayList;
import java.util.List;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tukano.api.Follow;
import tukano.api.Like;
import tukano.api.Short;
import tukano.api.User;
import tukano.api.service.util.Result;
import tukano.impl.Hibernate;

public class JavaShorts implements tukano.api.java.Shorts {
	final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public Result<Short> createShort(String userId, String password) {

        //User user = temos de fazer um pedido rest ao server de users para autenticar


        // TODO Auto-generated method stub

        return ok();
    }

    @Override
    public Result<Void> deleteShort(String shortId, String password) {
        if(badParam(shortId) || badParam(password))
            return error(BAD_REQUEST);

        //pedido rest para verificar a password

        Short vid = getShort(shortId).value();

        if(vid == null)
            return error(NOT_FOUND);

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

       var shortList = Hibernate.getInstance().sql("SELECT * FROM Short WHERE shortId = '" + userId + "'", Short.class);
       List<String> idList = new ArrayList<>(shortList.size());

        for(int i = 0; i < shortList.size(); i++)
            idList.add(i, shortList.get(i).getShortId());

        if(shortList.isEmpty())
            return error(NOT_FOUND);

        return ok(idList);
    }

    @Override
    public Result<Void> follow(String userId1, String userId2, boolean isFollowing, String password) {
        if(badParam(userId1) || badParam(userId2) || badParam(password))
            return error(BAD_REQUEST);

        //pedido rest com getUser(userId1, password)
        //User user1 = getUser(userId1);

        int followNum = Hibernate.getInstance().sql("SELECT * FROM Follow", Follow.class).size();

        if(isFollowing){
            Hibernate.getInstance().persist(new Follow(followNum, userId1, userId2));
        } else {
            var follow = Hibernate.getInstance().sql("SELECT * FROM Follow WHERE followerId = '"
                    + userId1  + "' AND followedId = '" + userId2 + "'", Follow.class);
            Hibernate.getInstance().delete(follow);
            //ou Hiberate.getInstance().delete(new Follow(userId1, userId2);
        }

        return ok();
    }

    @Override
    public Result<List<String>> followers(String userId, String password) {
        if(badParam(userId) || badParam(password))
            return error(BAD_REQUEST);

        //fazer um get user para verificar a password.

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

        //fazer um get user para verificar a password
        //User user = getUser(userId, password);

        var likeList = Hibernate.getInstance().sql("SELECT * FROM Like WHERE userId = '"
                + userId + "' AND shortId = '" + shortId + "'" , Like.class);

        if(isLiked){
            if(!likeList.isEmpty())
                return error(CONFLICT);

            var userLikes = Hibernate.getInstance().sql("SELECT * FROM Like WHERE userId = '"
                    + userId + "'", Like.class);

            int likeNum = userLikes.size();
            String likeId = userId.concat(String.valueOf(likeNum));

            Hibernate.getInstance().persist(new Like(likeId, userId, shortId));
        }else {
            if(likeList.isEmpty())
                return error(NOT_FOUND);

            Hibernate.getInstance().delete(likeList.get(0));
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
        //fazer um pedido getUser para verificação
        //getuser(ownerId, password);

        var likeList = Hibernate.getInstance().sql("SELECT * FROM Like WHERE shortId = '"
                + shortId + "'", Like.class);

        List<String> likeIdList = new ArrayList<>(likeList.size());
        for(int i = 0; i < likeIdList.size(); i++){
            likeIdList.add(i, likeList.get(i).getLikeId());
        }

        return ok(likeIdList);
    }

    @Override
    public Result<List<String>> getFeed(String userId, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getFeed'");
    }

    private boolean badParam(String str) {
		return str == null;
	}

	private boolean badShort(Short sh) {
		return sh == null || badParam(sh.getShortId()) || badParam(sh.getBlobUrl())
				|| badParam(sh.getOwnerId());
	}

    private boolean badUser(User user) {
        return user == null || badParam(user.getEmail()) || badParam(user.getDisplayName())
                || badParam(user.getPwd());
    }

    private boolean wrongPassword(User user, String password) {
        return !user.getPwd().equals(password);
    }

}
