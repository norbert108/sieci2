package zestaw6;

import java.io.IOException;
import java.util.Scanner;

import org.apache.zookeeper.*;

public class Executor implements Watcher, Runnable, DataMonitorListener
{
    private final String znode = "/znode_testowy";

    private DataMonitor dataMonitor;
    private ZooKeeper zooKeeper;

    private String command;
    private Process child;

    public Executor(String hostPort, String command) throws KeeperException, IOException {
        this.command = command;
        this.zooKeeper = new ZooKeeper(hostPort, 3000, this);
        this.dataMonitor = new DataMonitor(zooKeeper, znode, null, this);
    }

    /* delegate data listener events to data monitor */
    public void process(WatchedEvent event) {
        dataMonitor.process(event);
    }

    public void run() {
        // scan input for commands
        Scanner scanner = new Scanner(System.in);

        while(dataMonitor.isAlive()){
            String cm = scanner.next();
            if(cm.startsWith("show")){
                dataMonitor.printChildNodes(znode);
            }
        }
    }

    /* DataMonitorListener methods */
    public void created() {
        ProcessBuilder processBuilder = new ProcessBuilder(command);
        try {
            child = processBuilder.start();
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }

    public void deleted() {
        //stop process
        child.destroy();
        child = null;
    }

    public void childrenChanged(int children) {
        System.out.println("Number of children has changed, currently " + children);
    }

    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }
}
