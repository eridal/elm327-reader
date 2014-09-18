package elm327.reader;

public interface Result<T> {
    Command<T> command();
    T data();
    boolean isError();
}
