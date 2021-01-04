package bgu.spl.net.impl.BGRSServer;

public interface Instruction<T> {
    T process(Database database, T msg);
}
