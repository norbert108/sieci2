package zestaw6;

import java.util.Collections;
import java.util.List;

import org.apache.zookeeper.*;
import org.apache.zookeeper.AsyncCallback.StatCallback;
import org.apache.zookeeper.AsyncCallback.ChildrenCallback;
import org.apache.zookeeper.KeeperException.Code;
import org.apache.zookeeper.data.Stat;

public class DataMonitor implements Watcher, StatCallback, ChildrenCallback {

    private ZooKeeper zooKeeper;
    private String zNode;
    private DataMonitorListener listener;

    int childrenNumber = 0;

    boolean dead;

    public DataMonitor(ZooKeeper zk, String znode, Watcher chainedWatcher, DataMonitorListener listener) {
        this.zooKeeper = zk;
        this.zNode = znode;this.listener = listener;

        zooKeeper.exists(znode, true, this, null); //check node status asynchronously
    }

    public void process(WatchedEvent event) {
        String path = event.getPath();

        if(event.getType() == Event.EventType.NodeChildrenChanged){
            zooKeeper.getChildren(path, true, this, null);
        }
        else if(event.getType() == Event.EventType.NodeCreated){
            zooKeeper.exists(zNode, true, this, null);
        }
        else if(event.getType() == Event.EventType.NodeDeleted){
            if(path.equals(zNode))
                zooKeeper.exists(zNode, true, this, null);
        }
    }

    /**
     * Prints all children of node specified by parameter
     * @param path root node path
     */
    public void printChildNodes(String path){
        List<String> childrenList;

        try{
            childrenList = zooKeeper.getChildren(path, true);
        } catch (Exception e){
            e.printStackTrace();
            return;
        }

        Collections.sort(childrenList);

        System.out.println(path);
        for(String child : childrenList){
            printChildNodes(path+"/"+child);
        }
    }

    /**
     * StatCallback interface method
     */
    public void processResult(int rc, String path, Object ctx, Stat stat) {
        switch(rc){
            case Code.Ok: {
                if (path.equals(zNode)) {
                    listener.created();
                    zooKeeper.getChildren(zNode, true, this, null);
                }
                break;
            }
            case Code.NoNode: {
                if (path == zNode) {
                    listener.deleted();
                }
                break;
            }
            case Code.SessionExpired:
            case Code.NoAuth: {
                dead = true;
                listener.closing(rc);
            }
        }
    }

    /**
     * ChildrenCallback interface method
     */
    @Override
    public void processResult(int rc, String s, Object o, List<String> list) {
        switch(rc){
            case Code.SessionExpired:
            case Code.NoAuth:
                dead = true;
                listener.closing(rc);
            case Code.Ok:
                break;
        }

        int updatedChildrenNumber = countChildren(zNode);
        if(updatedChildrenNumber > childrenNumber) {
            listener.childrenChanged(updatedChildrenNumber);
        }
        childrenNumber = updatedChildrenNumber;
    }

    // TODO testowac miliard razy xD
    private int countChildren(String path){
        int childrenNumber = 0;

        try {
            List<String> childrenList = zooKeeper.getChildren(path, true);

            for (String child: childrenList) {
                childrenNumber += countChildren(path + "/" + child) + 1;
            }

            return childrenNumber;
        } catch (KeeperException | InterruptedException e) {
            e.printStackTrace();

            return 0; //TODO LOL
        }
    }

    public boolean isAlive(){
        return !dead;
    }
}