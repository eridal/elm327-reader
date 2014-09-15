package elm327.reader;


public interface Command<T> {
    String toMessage();
    T parse(String data);
}
