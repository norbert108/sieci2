package zestaw6;

public interface DataMonitorListener {
    void created();
    void deleted();
    void childrenChanged(int children);
    void closing(int rc);
}