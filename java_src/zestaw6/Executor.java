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

        try {
            zooKeeper.exists(znode, true);   // first check TODO czeba?
            System.out.println("It works!");
        } catch (Exception e){e.printStackTrace();}
    }

    /* delegate data listener events to data monitor */ // TODO lol
    public void process(WatchedEvent event) {
        dataMonitor.process(event);
    }

    public void run() {
        // scan input for commands
        Scanner scanner = new Scanner(System.in); //TODO moze sie posypac

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
        // TODO: wypisac cos?
        //stop process
        System.out.println("Oh my god, I'm dead! Why Lisa whyyyyyy?!!!111");
        child.destroy();
        child = null;
    }

    public void childrenChanged(int children) {
        System.out.println("Number of children has changed, currently " + children); //TODO zmodyfikowac zeby wypisywa³o tylko jak wzrosnie liczba childrenow
    }

    public void closing(int rc) {
        synchronized (this) {
            notifyAll();
        }
    }
}
