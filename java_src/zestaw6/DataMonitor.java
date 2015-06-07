import java.util.Arrays;

import org.apache.zookeeper.KeeperException;
import org.apache.zookeeper.WatchedEvent;
import org.apache.zookeeper.Watcher;
import org.apache.zookeeper.ZooKeeper;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.data.Stat;

public class DataMonitor implements Watcher, StatCallback {

    private ZooKeeper zooKeeper;
    private String zNode;
    private Watcher chainedWatcher;
    private DataMonitorListener listener;

    byte prevData[];

    boolean dead;

    public DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, DataMonitorListener listener) {
        this.zooKeeper = zk;
        this.zNode = znode;
        this.chainedWatcher = chainedWatcher;
        this.listener = listener;

        zooKeeper.exists(znode, true, this, null); //check node status asynchronously
    }

    public interface DataMonitorListener {
        void exists(byte data[]);
        void closing(int rc);
    }

    public void process(WatchedEvent event) {
        String path = event.getPath();
//        if (event.getType() == Event.EventType.None) {
//            // We are are being told that the state of the
//            // connection has changed
//            switch (event.getState()) {
//                case SyncConnected:
//                    // In this particular example we don't need to do anything
//                    // here - watches are automatically re-registered with
//                    // server and any watches triggered while the client was
//                    // disconnected will be delivered (in order of course)
//                    break;
//                case Expired:
//                    // It's all over
//                    dead = true;
//                    listener.closing(KeeperException.Code.SessionExpired);
//                    break;
//            }
//        } else {
            if (path != null && path.equals(zNode)) {
                // Something has changed on the node, let's find out
                zooKeeper.exists(zNode, true, this, null);
            }
//        }
//        if (chainedWatcher != null) {
//            chainedWatcher.process(event);
//        }
    }

    /**
     * StatCallback callback method, called by exists.
     */
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        boolean exists;
        switch (rc) {
            case Code.Ok:
                exists = true;
                break;
            case Code.NoNode:
                exists = false;
                break;
            case Code.SessionExpired:
            case Code.NoAuth:
                dead = true;
                listener.closing(rc);
//                return;
//            default:
                // Retry errors
//                zk.exists(znode, true, this, null);
//                return;
        }

        System.out.println("EXISTS CODE: " + rc);

//        byte b[] = null;
//        if (exists) {
//            try {
//                b = zk.getData(znode, false, null);
//            } catch (KeeperException e) {
//                // We don't need to worry about recovering now. The watch
//                // callbacks will kick off any exception handling
//                e.printStackTrace();
//            } catch (InterruptedException e) {
//                return;
//            }
//        }
//        if ((b == null && b != prevData)
//                || (b != null && !Arrays.equals(prevData, b))) {
//            listener.exists(b);
//            prevData = b;
//        }
    }
}