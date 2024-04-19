package tukano.impl.client;

import java.net.URI;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import tukano.api.java.Shorts;
// import tukano.impl.srv.Domain;
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

	static LoadingCache<URI, Shorts> shorts = CacheBuilder.newBuilder().maximumSize(CACHE_CAPACITY)
			.build(new CacheLoader<>() {
				@Override
				public Shorts load(URI uri) throws Exception {
					Shorts client;
					if (uri.toString().endsWith(REST))
						client = new RestShortsClient(uri);
					// else if (uri.toString().endsWith(GRPC))
					// 	client = new GrpcShortsClient(uri);
					else
						throw new RuntimeException("Unknown service type..." + uri);

					return new RetryShortsClient(client);
				}
			});

	public static Shorts get() {
		return get(String.format("%s", SERVICE));
	}

	public static Shorts get(String fullName) {
		URI[] uris = Discovery.getInstance().findUrisOf(fullName, 1);
		return getByUri(uris[0].toString());
	}

	public static Shorts getByUri(String uriString) {
		try {
			return shorts.get(URI.create(uriString));
		} catch (Exception x) {
			x.printStackTrace();
		}
		return null;
	}
}