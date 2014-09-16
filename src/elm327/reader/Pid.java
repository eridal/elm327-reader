package elm327.reader;

class Pid<T> implements Command<T> {

    public final Message message;
    public final Unit unit;
    public final Parser<T> parser;

    Pid(String code, Unit unit, Parser<T> parser) {
        this.unit = unit;
        this.parser = parser;
        message = Message.DATA(code);
    }

    @Override public Message message() {
        return message;
    }

    @Override public T parse(String data) {
        return parser.parse(data);
    }
}