package tukano.impl.client.common;

import tukano.api.Short;
import tukano.api.java.Shorts;
import tukano.api.service.util.Result;

import java.util.List;

public class RetryShortsClient extends RetryClient implements Shorts {

	final Shorts impl;

	public RetryShortsClient(Shorts impl) {
		this.impl = impl;
	}

	@Override
	public Result<Short> createShort(String userId, String password) {
		return reTry(() -> impl.createShort(userId, password));
	}

	@Override
	public Result<Void> deleteShort(String shortId, String password) {
		return reTry(() -> impl.deleteShort(shortId, password));
	}

	@Override
	public Result<Short> getShort(String shortId) {
		return reTry(() -> impl.getShort(shortId));
	}

	@Override
	public Result<List<String>> getShorts(String userId) {
		return reTry(() -> impl.getShorts(userId));
	}

	@Override
	public Result<Void> follow(String userId1, String userId2, boolean isFollowing, String password) {
		return reTry(() -> impl.follow(userId1, userId2, isFollowing, password));
	}

	@Override
	public Result<List<String>> followers(String userId, String password) {
		return reTry(() -> impl.followers(userId, password));
	}

	@Override
	public Result<Void> like(String shortId, String userId, boolean isLiked, String password) {
		return reTry(() -> impl.like(shortId, userId, isLiked, password));
	}

	@Override
	public Result<List<String>> likes(String shortId, String password) {
		return reTry(() -> impl.likes(shortId, password));
	}

	@Override
	public Result<List<String>> getFeed(String userId, String password) {
		return reTry(() -> impl.getFeed(userId, password));
	}

	/*@Override
	public Result<Void> deleteFollows(String userId) {
		return reTry(() -> impl.deleteFollows(userId));
	}*/

}