package elm327.reader;


public interface Command<T> {
    Message message();
    T parse(String data);
}
