package bgu.spl.net.impl.BGRSServer;

public interface Instruction<T> {
    public T process(Database database, T msg);
}
