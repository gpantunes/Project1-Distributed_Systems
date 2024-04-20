package tukano.impl.client;

import tukano.api.java.Users;
import tukano.impl.client.common.RetryUsersClient;
import tukano.impl.discovery.Discovery;
import tukano.impl.client.rest.RestUsersClient;

public class UsersClientFactory {
	private static final String SERVICE = "users";

	static Discovery discovery = Discovery.getInstance();

	public static Users getClient() {
		var Uri = discovery.findUrisOf(SERVICE, 1);
		var serverURI = Uri[0];
		if( String.valueOf(serverURI).endsWith("rest"))
			return new RetryUsersClient(new RestUsersClient(serverURI));
        else
			return null;//new GrpcUsersClient(serverURI);
	}
}

