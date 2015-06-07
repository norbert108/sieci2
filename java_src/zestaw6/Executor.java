/**
 * A simple example program to use DataMonitor to start and
 * stop executables based on a znode. The program watches the
 * specified znode and saves the data that corresponds to the
 * znode in the filesystem. It also starts the specified program
 * with the specified arguments when the znode exists and kills
 * the program if the znode goes away.
 */
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import org.apache.zookeeper.*;

public class Executor implements Watcher, Runnable
{
    private final String znode = "znode_testowy";

    private DataMonitor dataMonitor;
    private ZooKeeper zooKeeper;

    String filename;

    String exec[];

    Process child;

    public Executor(String hostPort, String filename, String exec[])
            throws KeeperException, IOException {

        this.filename = filename;
//        this.exec = exec;

        String connectionString = hostPort;
        this.zooKeeper = new ZooKeeper(connectionString, 3000, this);
        try {
            zooKeeper.exists(znode, true);
            System.out.println("xD");
        } catch (Exception e){e.printStackTrace();}
//        this.dataMonitor = new DataMonitor(zooKeeper, znode, null, this);
    }

    /* process events dispatched to watcher */
    public void process(WatchedEvent event) {
        String path = event.getPath();

        if (path != null && path.equals(znode)) {
            // Something has changed on the node, let's find out
            try {
                zooKeeper.exists(znode, true);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void run() {
        try {
            synchronized (this) {
                while (!dataMonitor.dead) {
                    wait();
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }


//    static class StreamWriter extends Thread {
//        OutputStream os;
//
//        InputStream is;
//
//        StreamWriter(InputStream is, OutputStream os) {
//            this.is = is;
//            this.os = os;
//            start();
//        }
//
//        public void run() {
//            byte b[] = new byte[80];
//            int rc;
//            try {
//                while ((rc = is.read(b)) > 0) {
//                    os.write(b, 0, rc);
//                }
//            } catch (IOException e) {
//            }
//
//        }
//    }

//    public void exists(byte[] data) {
//        if (data == null) {
//            if (child != null) {
//                System.out.println("Killing process");
//                child.destroy();
//                try {
//                    child.waitFor();
//                } catch (InterruptedException e) {
//                }
//            }
//            child = null;
//        } else {
//            if (child != null) {
//                System.out.println("Stopping child");
//                child.destroy();
//                try {
//                    child.waitFor();
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
//                }
//            }
//            try {
//                FileOutputStream fos = new FileOutputStream(filename);
//                fos.write(data);
//                fos.close();
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//            try {
//                System.out.println("Starting child");
//                child = Runtime.getRuntime().exec(exec);
//                new StreamWriter(child.getInputStream(), System.out);
//                new StreamWriter(child.getErrorStream(), System.err);
//            } catch (IOException e) {
//                e.printStackTrace();
//            }
//        }
//    }
}
