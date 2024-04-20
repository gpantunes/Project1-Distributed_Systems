package tukano.impl.client.rest;

import java.net.URI;
import java.util.List;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import tukano.api.User;
import tukano.api.java.Users;
import tukano.api.rest.RestUsers;
import tukano.api.service.util.Result;

public class RestUsersClient extends RestClient implements Users {
	private static final String PASSWORD = "pwd";
	private static final String QUERY = "query";

	public RestUsersClient(URI serverUri) {
		super(serverUri, RestUsers.PATH);
	}

	@Override
	public Result<String> createUser(User user) {
		Response r = target
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(user, MediaType.APPLICATION_JSON));
		return super.responseContents(r, Status.OK, new GenericType<String>() {
		});
	}

	@Override
	public Result<User> getUser(String userId, String password) {
		Response r = target.path(userId)
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<User>() {
		});
	}

	@Override
	public Result<User> updateUser(String userId, String password, User user) {
		Response r = target.path(userId)
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.put(Entity.entity(user, MediaType.APPLICATION_JSON));

		return super.responseContents(r, Status.OK, new GenericType<User>() {
		});
	}

	@Override
	public Result<User> deleteUser(String userId, String password) {
		Response r = target.path(userId)
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.delete();
		return super.responseContents(r, Status.OK, new GenericType<User>() {
		});
	}

	@Override
	public Result<List<User>> searchUsers(String pattern) {
		Response r = target.path("/")
				.queryParam(QUERY, pattern)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();

		return super.responseContents(r, Status.OK, new GenericType<List<User>>() {
		});
	}
}