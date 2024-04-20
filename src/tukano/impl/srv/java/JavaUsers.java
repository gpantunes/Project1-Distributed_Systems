package tukano.impl.srv.java;

import static tukano.api.service.util.Result.error;
import static tukano.api.service.util.Result.ok;
import static tukano.api.service.util.Result.ErrorCode.BAD_REQUEST;
import static tukano.api.service.util.Result.ErrorCode.CONFLICT;
import static tukano.api.service.util.Result.ErrorCode.FORBIDDEN;
import static tukano.api.service.util.Result.ErrorCode.NOT_FOUND;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.logging.Logger;

import tukano.api.User;
import tukano.api.java.Shorts;
import tukano.api.service.util.Result;
import tukano.impl.Hibernate;
import tukano.impl.client.ShortsClientFactory;

public class JavaUsers implements tukano.api.java.Users {
	final ExecutorService executor = Executors.newCachedThreadPool();

	private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

	Shorts client = ShortsClientFactory.getClient();

	@Override
	public Result<String> createUser(User user) {
		if (badUser(user))
			return error(BAD_REQUEST);

		var userId = user.getUserId();

		if (!Hibernate.getInstance().sql("SELECT * FROM User WHERE userId = '" + userId + "'", User.class).isEmpty())
			return error(CONFLICT);

		Hibernate.getInstance().persist(user);
		return ok(userId);
	}

	@Override
	public Result<User> getUser(String userId, String pwd) {
		if (badParam(userId))
			return error(BAD_REQUEST);

		var userList = Hibernate.getInstance().sql("SELECT * FROM User WHERE userId = '"+ userId + "'", User.class);

		if (userList.isEmpty())
			return error(NOT_FOUND);

		User user = userList.get(0);

		Log.info("############################ password:" + pwd);

		if (badParam(pwd) || wrongPassword(user, pwd))
			return error(FORBIDDEN);
		else{
			Log.info("[[[[[[[[[[[[[[[[[[o get user passou " + user.getUserId());
			return ok(user);
		}

	}

	@Override
	public Result<User> updateUser(String userId, String pwd, User user) {
		if (badParam(userId))
			return error(BAD_REQUEST);

		var userList = Hibernate.getInstance().sql("SELECT * FROM User WHERE userId = '" + userId + "'", User.class);

		if (userList.isEmpty())
			return error(NOT_FOUND);

		User oldUser = userList.get(0);

		Log.info("(" + oldUser.getUserId() + ", " + oldUser.getPwd() + ", "
				+ oldUser.getEmail() + ", " + oldUser.getDisplayName() + ")");

		if (badParam(pwd) || wrongPassword(oldUser, pwd))
			return error(FORBIDDEN);

		oldUser.setPwd(Objects.requireNonNullElse(user.getPwd(), oldUser.getPwd()));
		oldUser.setEmail(Objects.requireNonNullElse(user.getEmail(), oldUser.getEmail()));
		oldUser.setDisplayName(Objects.requireNonNullElse(user.getDisplayName(), oldUser.getDisplayName()));

		Hibernate.getInstance().update(oldUser);

		return ok(oldUser);
	}

	@Override
	public Result<User> deleteUser(String userId, String pwd) {
		Log.info("################# delete user foi chamado");
		if (badParam(userId))
			return error(BAD_REQUEST);

		var userList = Hibernate.getInstance().sql("SELECT * FROM User WHERE userId = '" + userId + "'", User.class);
		if(userList.isEmpty())
			return error(NOT_FOUND);
		User user = userList.get(0);
		if (badParam(pwd) || wrongPassword(user, pwd))
			return error(FORBIDDEN);

		/*var likesResult = client.deleteLikes(userId);

		Log.info("//////////////////// resultado dos likes " + likesResult.isOK());*/

		/*Result<Void> deleteLikesResult = client.deleteLikes(userId);

		if (!deleteLikesResult.isOK()) {
			Log.info("Error deleting shorts for user: " + userId);
			return Result.error(Result.ErrorCode.NOT_FOUND);
		}else Log.info("{{{{{{{{{{{{{{{ antes de chamar o delete short " + pwd);


		/*var shortList = client.getShorts(userId).value();

		for(int i = 0; i < shortList.size(); i++){
			client.deleteShort(shortList.get(i), pwd);
		}

		Log.info("%%%%%%%%%%%% depois de apagar shorts");*/

		Hibernate.getInstance().delete(user);

		return ok(user);
	}

	@Override
	public Result<List<User>> searchUsers(String pattern) {
		if (badParam(pattern))
			return error(BAD_REQUEST);

		pattern = pattern.toLowerCase();

		var hits = Hibernate.getInstance().sql("SELECT * FROM User WHERE LOWER(userId) LIKE '%" + pattern + "%'", User.class);

		return ok(hits);
	}


	private boolean badParam(String str) {
		return str == null;
	}

	private boolean badUser(User user) {
		return user == null || badParam(user.getEmail()) || badParam(user.getDisplayName())
				|| badParam(user.getPwd());
	}

	private boolean wrongPassword(User user, String password) {
		return !user.getPwd().equals(password);
	}

}
