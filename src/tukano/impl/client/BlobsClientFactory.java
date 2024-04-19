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
	

}