package tukano.impl.srv.java;

import static tukano.api.service.util.Result.error;
import static tukano.api.service.util.Result.ok;
import static tukano.api.service.util.Result.ErrorCode.BAD_REQUEST;
import static tukano.api.service.util.Result.ErrorCode.CONFLICT;
import static tukano.api.service.util.Result.ErrorCode.FORBIDDEN;
import static tukano.api.service.util.Result.ErrorCode.NOT_FOUND;

import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import tukano.api.User;
import tukano.api.service.util.Result;
import tukano.impl.Hibernate;

public class JavaUsers implements tukano.api.java.Users {
	final ExecutorService executor = Executors.newCachedThreadPool();

	@Override
	public Result<String> createUser(User user) {
		if (badUser(user))
			return error(BAD_REQUEST);

		var userId = user.getUserId();

		if (Hibernate.getInstance().sql("SELECT * FROM User WHERE userId = '" + userId + "'", User.class).isEmpty())
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
		else
			return ok(user);
	}

	//ter em atenção aos campos null do que não é para alterar
	@Override
	public Result<User> updateUser(String userId, String pwd, User user) {
		if (badParam(userId))
			return error(BAD_REQUEST);

		var userList = Hibernate.getInstance().sql("SELECT * FROM User WHERE userId = '" + userId + "'", User.class);

		if (userList.isEmpty())
			return error(NOT_FOUND);

		User oldUser = userList.get(0);

		if (badParam(pwd) || wrongPassword(user, pwd))
			return error(FORBIDDEN);


		if(!oldUser.getPwd().equals(user.getPwd())) oldUser.setPwd(user.getPwd());
		if(!oldUser.getEmail().equals(user.getEmail())) oldUser.setEmail(user.getEmail());
		if(!oldUser.getDisplayName().equals(user.getDisplayName())) oldUser.setDisplayName(user.getDisplayName());

		Hibernate.getInstance().update(oldUser);

		return ok(oldUser);
	}

	@Override
	public Result<User> deleteUser(String userId, String pwd) {
		if (badParam(userId))
			return error(BAD_REQUEST);

		var userList = Hibernate.getInstance().sql("SELECT * FROM User WHERE userId = '" + userId + "'", User.class);

		if(userList.isEmpty())
			return error(CONFLICT);

		User user = userList.get(0);

		if (badParam(pwd) || wrongPassword(user, pwd))
			return error(FORBIDDEN);

		Hibernate.getInstance().delete(user);

		//TODO:we need to delete all shorts associated with this user

		return ok(user);
	}

	@Override
	public Result<List<User>> searchUsers(String pattern) {
		if (badParam(pattern))
			return error(BAD_REQUEST);

		var hits = Hibernate.getInstance().sql("SELECT * FROM User WHERE userId LIKE  = '%" + pattern + "%'", User.class);

		/*var hits = users.values()
				.stream()
				.filter(u -> u.getDisplayName().toLowerCase().contains(pattern.toLowerCase()))
				.map(User::secureCopy)
				.collect(Collectors.toList());*/

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
