package tukano.impl.client.rest;

import java.net.URI;
import java.util.List;

import jakarta.ws.rs.client.Entity;
import jakarta.ws.rs.core.GenericType;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;
import jakarta.ws.rs.core.Response.Status;

import tukano.api.Short;
import tukano.api.java.Shorts;
import tukano.api.rest.RestShorts;
import tukano.api.service.util.Result;

public class RestShortsClient extends RestClient implements Shorts {
	private static final String PASSWORD = "password";
	String USER_ID = "userId";
	String USER_ID1 = "userId1";
	String USER_ID2 = "userId2";
	String SHORT_ID = "shortId";

	String PWD = "pwd";
	String FEED = "/feed";

	String LIKES = "/likes";
	String SHORTS = "/shorts";
	String FOLLOWERS = "/followers";

	public RestShortsClient(URI serverUri) {
		super(serverUri, RestShorts.PATH);
	}

	@Override
	public Result<Short> createShort(String userId, String password) {
		Response r = target
				.path("/" + "{" + userId + "}")
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(Entity.entity(new GenericType<byte[]>() {
				}, MediaType.APPLICATION_JSON)); // TODO: nao sei es esta correto!!!!!!!!!!!!!
		return super.responseContents(r, Status.OK, new GenericType<Short>() {
		});
	}

	@Override
	public Result<Void> deleteShort(String shortId, String password) {
		Response r = target
				.path("/" + "{" + shortId + "}")
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.delete();
		return super.responseContents(r, Status.NO_CONTENT, null);
	}

	@Override
	public Result<Short> getShort(String shortId) {
		Response r = target.path("/")
				.path("{" + shortId + "}")
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<Short>() {
		});
	}

	@Override
	public Result<List<String>> getShorts(String userId) {
		Response r = target.path("/")
				.path("{" + userId + "}" + SHORTS)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<List<String>>() {
		});
	}

	@Override
	public Result<Void> follow(String userId1, String userId2, boolean isFollowing, String password) {
		Response r = target
				.path("/{" + userId1 + "}/{" + userId2 + "}" + FOLLOWERS)
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(null); // TODO: nao sei o que por dentro do post
		return super.responseContents(r, Status.NO_CONTENT, null);
	}

	@Override
	public Result<List<String>> followers(String userId, String password) {
		Response r = target.path("/")
				.path("{" + userId + "}" + FOLLOWERS)
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<List<String>>() {
		});
	}

	@Override
	public Result<Void> like(String shortId, String userId, boolean isLiked, String password) {
		Response r = target.path("/")
				.path("{" + shortId + "}/{" + userId + "}" + LIKES)
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.post(null);
		return super.responseContents(r, Status.NO_CONTENT, null);
	}

	@Override
	public Result<List<String>> likes(String shortId, String password) {
		Response r = target.path("/")
				.path("{" + shortId + "}" + LIKES)
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<List<String>>() {
		});
	}

	@Override
	public Result<List<String>> getFeed(String userId, String password) {
		Response r = target.path("/")
				.path("{" + userId + "}" + FEED)
				.queryParam(PASSWORD, password)
				.request()
				.accept(MediaType.APPLICATION_JSON)
				.get();
		return super.responseContents(r, Status.OK, new GenericType<List<String>>() {
		});
	}

}
