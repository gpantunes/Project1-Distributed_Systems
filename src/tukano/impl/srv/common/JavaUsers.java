package tukano.impl.srv.common;

import static tukano.api.service.util.Result.error;
import static tukano.api.service.util.Result.ok;
import static tukano.api.service.util.Result.ErrorCode.BAD_REQUEST;
import static tukano.api.service.util.Result.ErrorCode.CONFLICT;
import static tukano.api.service.util.Result.ErrorCode.FORBIDDEN;
import static tukano.api.service.util.Result.ErrorCode.NOT_FOUND;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

import tukano.api.User;
import tukano.api.service.util.Result;

public class JavaUsers implements tukano.api.java.Users {
	final protected Map<String, User> users = new ConcurrentHashMap<>();
	final ExecutorService executor = Executors.newCachedThreadPool();

	@Override
	public Result<String> createUser(User user) {
		if (badUser(user))
			return error(BAD_REQUEST);

		var userId = user.getUserId();
		var res = users.putIfAbsent(userId, user);

		if (res != null)
			return error(CONFLICT);
		else
			return ok(userId);
	}

	@Override
	public Result<User> getUser(String userId, String pwd) {
		if (badParam(userId))
			return error(BAD_REQUEST);

		var user = users.get(userId);

		if (user == null)
			return error(NOT_FOUND);

		if (badParam(pwd) || wrongPassword(user, pwd))
			return error(FORBIDDEN);
		else
			return ok(user);
	}

	@Override
	public Result<User> updateUser(String userId, String pwd, User user) {
		var userToBeUpdated = users.get(userId);

		if (userToBeUpdated == null)
			return error(NOT_FOUND);

		if (badParam(pwd) || wrongPassword(userToBeUpdated, pwd))
			return error(FORBIDDEN);
		else {
			userToBeUpdated.updateUser(user);
			return ok(userToBeUpdated);
		}
	}

	@Override
	public Result<User> deleteUser(String userId, String pwd) {
		var user = users.get(userId);

		if (user == null)
			return error(NOT_FOUND);

		if (badParam(pwd) || wrongPassword(user, pwd))
			return error(FORBIDDEN);
		else {
			users.remove(userId);

			//TODO:we need to delete all shorts associated with this user

			return ok(user);
		}
	}

	@Override
	public Result<List<User>> searchUsers(String pattern) {
		if (badParam(pattern))
			return error(BAD_REQUEST);

		var hits = users.values()
				.stream()
				.filter(u -> u.getDisplayName().toLowerCase().contains(pattern.toLowerCase()))
				.map(User::secureCopy)
				.collect(Collectors.toList());

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
