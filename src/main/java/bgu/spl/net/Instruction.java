package bgu.spl.net;

public interface Instruction<T> {
    public T process(Database database, T msg);
}
