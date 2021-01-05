package bgu.spl.net.impl.BGRSServer;

public interface Instruction<T> {
    T process(T msg);
}
