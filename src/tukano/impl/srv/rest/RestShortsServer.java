package tukano.impl.srv.rest;

import org.glassfish.jersey.jdkhttp.JdkHttpServerFactory;
import org.glassfish.jersey.server.ResourceConfig;
import tukano.impl.rest.RestShortsClass;

import java.net.InetAddress;
import java.net.URI;
import java.util.logging.Logger;

public class RestShortsServer {

    private static Logger Log = Logger.getLogger(RestShortsServer.class.getName());

    static {
        System.setProperty("java.net.preferIPv4Stack", "true");
    }

    public static final int PORT = 4567;
    public static final String SERVICE = "ShortsService";
    private static final String SERVER_URI_FMT = "http://%s:%s/rest";

    public static void main(String[] args) {
        try {
            ResourceConfig config = new ResourceConfig();
            config.register(  RestShortsClass.class );

            String ip = InetAddress.getLocalHost().getHostAddress();
            String serverURI = String.format(SERVER_URI_FMT, ip, PORT);
            JdkHttpServerFactory.createHttpServer(URI.create(serverURI), config);

            Log.info(String.format("%s Short server ready @ %s\n", SERVICE, serverURI));

            // More code can be executed here...
        } catch (Exception e) {
            Log.severe(e.getMessage());
        }
    }
}
