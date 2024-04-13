package tukano.impl.discovery;

import tukano.impl.utils.Sleep;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;
import java.util.Enumeration;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;

public class Discovery {

    private static Logger Log = Logger.getLogger(Discovery.class.getName());
    private static final String DELIMITER = "\t";

    static {
        // addresses some multicast issues on some TCP/IP stacks
        System.setProperty("java.net.preferIPv4Stack", "true");
        // summarizes the logging format
        System.setProperty("java.util.logging.SimpleFormatter.format", "%4$s: %5$s");
    }

    static final InetSocketAddress DISCOVERY_ADDR = new InetSocketAddress("226.226.226.226", 2266);
    static final int DISCOVERY_PERIOD = 1000;
    static final int DISCOVERY_TIMEOUT = 5000;

    private static Discovery instance;

    final Map<String, Set<URI>> discoveries = new ConcurrentHashMap<>();

    synchronized public static Discovery getInstance() {
        if( instance == null ) {
            instance = new Discovery();
            new Thread( instance::listener ).start();
        }
        return instance;
    }


    public void announce(String serviceName, String serviceURI) {
        Log.info(String.format("Starting Discovery announcements on: %s for: %s -> %s\n", DISCOVERY_ADDR, serviceName, serviceURI));

        byte[] pktBytes = String.format("%s%s%s", serviceName, DELIMITER, serviceURI).getBytes();

        DatagramPacket pkt = new DatagramPacket(pktBytes, pktBytes.length, DISCOVERY_ADDR);
        new Thread(() -> {
            try (DatagramSocket ds = new DatagramSocket()) {
                for (;;) {
                    ds.send(pkt);
                    Thread.sleep(DISCOVERY_PERIOD);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }).start();
    }

    public void listener() {
        Log.info(String.format("Starting discovery on multicast group: %s, port: %d\n", DISCOVERY_ADDR.getAddress(), DISCOVERY_ADDR.getPort()));

        final int MAX_DATAGRAM_SIZE = 65535;

        var pkt = new DatagramPacket(new byte[MAX_DATAGRAM_SIZE], MAX_DATAGRAM_SIZE);

        try (var ms = new MulticastSocket(DISCOVERY_ADDR.getPort())) {
            joinGroupInAllInterfaces(ms);
            for(;;) {
                try {
                    pkt.setLength(MAX_DATAGRAM_SIZE);
                    ms.receive(pkt);

                    var tokens = new String(pkt.getData(), 0, pkt.getLength()).split(DELIMITER);
                    Log.finest( "Received: " + Arrays.asList(tokens) + "\n");

                    if (tokens.length == 2) {

                        var name = tokens[0];
                        var uri = URI.create( tokens[1]);

                        discoveries.computeIfAbsent(name, (k) -> ConcurrentHashMap.newKeySet()).add( uri );
                    }
                } catch (IOException e) {
                    Sleep.ms(DISCOVERY_PERIOD);
                    Log.finest("Still listening...");
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    public URI[] findUrisOf(String serviceName, int minRepliesNeeded) {
        Log.info(String.format("Waiting for discovery for: %s\n", serviceName));

        for(;;) {
            var results = discoveries.get( serviceName );
            if( results != null && results.size() >= minRepliesNeeded )
                return results.toArray( new URI[ results.size() ]);
            else
                Sleep.ms( DISCOVERY_PERIOD );
        }
    }

    static private void joinGroupInAllInterfaces(MulticastSocket ms) throws SocketException {
        Enumeration<NetworkInterface> ifs = NetworkInterface.getNetworkInterfaces();
        while (ifs.hasMoreElements()) {
            NetworkInterface xface = ifs.nextElement();
            try {
                ms.joinGroup(DISCOVERY_ADDR, xface);
                ;
            } catch (Exception x) {
                x.printStackTrace();
            }
        }
    }

}
