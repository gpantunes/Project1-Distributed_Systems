package tukano.impl.srv.java;

import static tukano.api.service.util.Result.ErrorCode.BAD_REQUEST;
import static tukano.api.service.util.Result.ErrorCode.NOT_FOUND;
import static tukano.api.service.util.Result.error;
import static tukano.api.service.util.Result.ok;

import java.util.Base64;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tukano.api.Short;
import tukano.api.User;
import tukano.api.service.util.Result;
import tukano.impl.Hibernate;

public class JavaShorts implements tukano.api.java.Shorts {
	final protected Map<String, Short> shorts = new ConcurrentHashMap<>();
	final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public Result<Short> createShort(String userId, String password) {

        //User user = temos de fazer um pedido rest ao server de users

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

        //fazer um pedido getUser e sacar os shorts do shortMap do user
        //passar de hash map para lista

        return ok();
    }

    @Override
    public Result<Void> follow(String userId1, String userId2, boolean isFollowing, String password) {
        if(badParam(userId1) || badParam(userId2) || badParam(password))
            return error(BAD_REQUEST);

        //fazer dois getUser

        User user1 = getUser(userId1);
        User user2 = getUser(userId2);

        if(isFollowing){
            user1.addFollower(user2);
            user2.addToFollowers(user1);
        } else {
            user1.removeFollower(user2);
            user2.removeFromFollowers(user1);
        }

        //fazer pedidos updateUser para os dois users

        return ok();
    }

    @Override
    public Result<List<String>> followers(String userId, String password) {
        if(badParam(userId) || badParam(password))
            return error(BAD_REQUEST);

        //fazer um getUser e meter numa lista os users presentes no mapa de followers

        return ok();
    }

    @Override
    public Result<Void> like(String shortId, String userId, boolean isLiked, String password) {
        if(badParam(shortId) || badParam(userId) || badParam(password))
            return error(BAD_REQUEST);

        Short vid = getShort(shortId).value();

        if(vid == null)
            return error(NOT_FOUND);

        //fazer um pedido getUser

        User user = getUser(userId);
        user.addLike(shortList.get(0));

        return ok();
    }

    @Override
    public Result<List<String>> likes(String shortId, String password) {
        if(badParam(shortId) || badParam(password))
            return error(BAD_REQUEST);

        Short vid = getShort(shortId).value();

        if(vid == null)
            return error(NOT_FOUND);

        //fazer um pedido getUser
        //passar o mapa de likes para lista



        return ok();
    }

    @Override
    public tukano.api.java.Result<List<String>> getFeed(String userId, String password) {
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
