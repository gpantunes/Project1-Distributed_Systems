package tukano.impl.srv.java;

import static tukano.api.service.util.Result.ErrorCode.BAD_REQUEST;
import static tukano.api.service.util.Result.error;
import static tukano.api.service.util.Result.ok;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tukano.api.Short;
import tukano.api.User;
import tukano.api.service.util.Result;

public class JavaShorts implements tukano.api.java.Shorts {
	final protected Map<String, Short> shorts = new ConcurrentHashMap<>();
	final ExecutorService executor = Executors.newCachedThreadPool();

    @Override
    public Result<Short> createShort(String userId, String password) {

        User user =

        // TODO Auto-generated method stub

        return ok();
    }

    @Override
    public Result<Void> deleteShort(String shortId, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'deleteShort'");
    }

    @Override
    public tukano.api.java.Result<Short> getShort(String shortId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getShort'");
    }

    @Override
    public tukano.api.java.Result<List<String>> getShorts(String userId) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'getShorts'");
    }

    @Override
    public tukano.api.java.Result<Void> follow(String userId1, String userId2, boolean isFollowing, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'follow'");
    }

    @Override
    public tukano.api.java.Result<List<String>> followers(String userId, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'followers'");
    }

    @Override
    public tukano.api.java.Result<Void> like(String shortId, String userId, boolean isLiked, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'like'");
    }

    @Override
    public tukano.api.java.Result<List<String>> likes(String shortId, String password) {
        // TODO Auto-generated method stub
        throw new UnsupportedOperationException("Unimplemented method 'likes'");
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
