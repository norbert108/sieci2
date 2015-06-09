package zestaw6;

import org.apache.zookeeper.KeeperException;
import java.io.IOException;

public class ClientRunner {

    public static void main(String... args) {

        if (args.length < 2) {
            System.err.println("Usage: client program_name host:port");
            System.exit(2);
        }
        String filename = args[0];
        String host = args[1];

        try {
            new Executor(host, filename).run();
        } catch (IOException | KeeperException e) {
            e.printStackTrace();
        }
    }
}
