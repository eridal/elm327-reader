package elm327.reader;

class Messages {

    static Message AT(String command) {
        return new Message("AT" + command);
    }

    static Message DATA(String command) {
        return new Message("01" + command);
    }
}
