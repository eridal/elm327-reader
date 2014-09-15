package elm327.reader;

class Message {

    private final String code;

    private Message(String code) {
        this.code = code;
    }

    public String code() {
        return code;
    }

    static Message AT(String command) {
        return new Message("AT" + command);
    }

    static Message DATA(String command) {
        return new Message("01" + command);
    }
}
