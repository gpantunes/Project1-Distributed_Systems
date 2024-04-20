package tukano.impl.srv.java;

import static tukano.api.service.util.Result.error;
import static tukano.api.service.util.Result.ok;
import static tukano.api.service.util.Result.ErrorCode.BAD_REQUEST;
import static tukano.api.service.util.Result.ErrorCode.CONFLICT;
import static tukano.api.service.util.Result.ErrorCode.FORBIDDEN;
import static tukano.api.service.util.Result.ErrorCode.NOT_FOUND;

import java.util.List;
import java.util.Objects;
import java.util.logging.Logger;

import tukano.api.User;
import tukano.api.service.util.Result;
import tukano.impl.Hibernate;


public class JavaUsers implements tukano.api.java.Users {

	private static Logger Log = Logger.getLogger(JavaUsers.class.getName());

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

		if (badParam(pwd) || wrongPassword(user, pwd))
			return error(FORBIDDEN);
		else{
			return ok(user);
		}

	}

	@Override
	public Result<User> updateUser(String userId, String pwd, User user) {
		if (badParam(userId) || user.getuserId != null)
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
		if (badParam(userId))
			return error(BAD_REQUEST);

		var userList = Hibernate.getInstance().sql("SELECT * FROM User WHERE userId = '" + userId + "'", User.class);
		if(userList.isEmpty())
			return error(NOT_FOUND);
		User user = userList.get(0);
		if (badParam(pwd) || wrongPassword(user, pwd))
			return error(FORBIDDEN);

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
