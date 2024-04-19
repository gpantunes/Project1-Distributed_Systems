package tukano.impl.client;

import java.net.URI;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import tukano.api.java.Shorts;
import tukano.api.java.Users;
import tukano.impl.client.rest.RestUsersClient;
import tukano.impl.discovery.Discovery;
import tukano.impl.client.rest.RestShortsClient;
// import tukano.impl.client.grpc.GrpcShortsClient;
import tukano.impl.client.common.RetryShortsClient;

public class ShortsClientFactory {
	private static final String SERVICE = "shorts";
	private static final String REST = "/rest";
	private static final String GRPC = "/grpc";

	private static final long CACHE_CAPACITY = 10;

	static Discovery discovery = Discovery.getInstance();

	public static Shorts getClient() {
		var Uri = discovery.findUrisOf("users", 1);
		var serverURI = Uri[0];
		if( String.valueOf(serverURI).endsWith("rest"))
			return new RestShortsClient(serverURI);
		else
			return null;//new GrpcShortsClient(serverURI);
	}
}