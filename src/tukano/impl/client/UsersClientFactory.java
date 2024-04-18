package tukano.impl.client;

import tukano.api.java.Users;
import tukano.impl.discovery.Discovery;
import tukano.impl.client.rest.RestUsersClient;

public class UsersClientFactory {
	private static final String SERVICE = "users";
	private static final String REST = "/rest";
	private static final String GRPC = "/grpc";

	private static final long CACHE_CAPACITY = 10;

	static Discovery discovery = new Discovery();

	public static Users getClient() {
		var Uri = discovery.findUrisOf("users", 1);
		var serverURI = Uri[0];
		if( String.valueOf(serverURI).endsWith("rest"))
			return new RestUsersClient( serverURI );
        else
			return null;//new GrpcUsersClient( serverURI );
	}
}

