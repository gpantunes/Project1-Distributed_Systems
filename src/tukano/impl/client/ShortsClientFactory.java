package tukano.impl.client;

import tukano.api.java.Shorts;
import tukano.impl.discovery.Discovery;
import tukano.impl.client.rest.RestShortsClient;
import tukano.impl.client.common.RetryShortsClient;

public class ShortsClientFactory {
	private static final String SERVICE = "shorts";

	static Discovery discovery = Discovery.getInstance();

	public static Shorts getClient() {
		var Uri = discovery.findUrisOf(SERVICE, 1);
		var serverURI = Uri[0];
		if( String.valueOf(serverURI).endsWith("rest"))
			return new RetryShortsClient(new RestShortsClient(serverURI));
		else
			return null;//new GrpcShortsClient(serverURI);
	}
}