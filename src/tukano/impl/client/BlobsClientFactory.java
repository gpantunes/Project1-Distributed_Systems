package tukano.impl.client;

import java.net.URI;

import com.google.common.cache.CacheBuilder;
import com.google.common.cache.CacheLoader;
import com.google.common.cache.LoadingCache;

import tukano.api.java.Blobs;
import tukano.impl.discovery.Discovery;
import tukano.impl.client.rest.RestBlobsClient;
// import tukano.impl.client.grpc.GrpcBlobsClient;
import tukano.impl.client.common.RetryBlobsClient;

public class BlobsClientFactory {

	private static final String SERVICE = "blobs";
	private static final String REST = "/rest";
	private static final String GRPC = "/grpc";

	private static final long CACHE_CAPACITY = 10;
	
	static LoadingCache<URI, Blobs> blobs = CacheBuilder.newBuilder().maximumSize(CACHE_CAPACITY)
			.build(new CacheLoader<>() {
				@Override
				public Blobs load(URI uri) throws Exception {
					Blobs client;
					if (uri.toString().endsWith(REST))
						client = new RestBlobsClient(uri);
					// else if (uri.toString().endsWith(GRPC))
					// 	client = new GrpcBlobsClient(uri);
					else
						throw new RuntimeException("Unknown service type..." + uri);
					
					return new RetryBlobsClient(client);
				}
			});
	
	public static Blobs get() {
		return get(String.format("%s", SERVICE));
	}
	
	public static Blobs get( String fullname ) {
		URI[] uris = Discovery.getInstance().findUrisOf(String.format("%s", SERVICE), 1);
		return with(uris[0].toString());
	}
	
	public static Blobs with(String uriString) {
		return blobs.getUnchecked( URI.create(uriString));					
	}
}