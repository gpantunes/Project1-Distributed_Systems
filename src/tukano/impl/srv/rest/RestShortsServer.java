package tukano.impl.srv.rest;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import tukano.impl.discovery.Discovery;
import tukano.impl.rest.RestShortsClass;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

public class RestShortsServer {

    private static Logger Log = Logger.getLogger(RestShortsServer.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    private static Discovery discovery = Discovery.getInstance();
    private static final String USER_SERVICE = "users";
    private static final String SHORT_SERVICE = "shorts";
    private static final String BLOB_SERVICE = "blobs";

    public static final int PORT = 4567;
    private static final String SERVER_URI_FMT = "http://%s:%s/rest";

    public static void main(String[] args) {
        try {
            ResourceConfig config = new ResourceConfig();
            config.register(  RestShortsClass.class );

            String ip = InetAddress.getLocalHost().getHostAddress();
            String serverURI = String.format(SERVER_URI_FMT, ip, PORT);
            JdkHttpServerFactory.createHttpServer(URI.create(serverURI), config);

            Log.info(String.format("%s Short server ready @ %s\n", SHORT_SERVICE, serverURI));

            discovery.announce(SHORT_SERVICE, serverURI);

        } catch (Exception e) {
            Log.severe(e.getMessage());
        }
    }
}
