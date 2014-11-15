package elm327.reader;

class Messages {

    static Message AT(String command) {
        return new Message("AT" + command);
    }

    static Message DATA(String command) {
        return new Message("01" + command + "1");
    }

    static Message readDTC() {
        return new Message("03");
    }

    static Message clearDTC() {
        return new Message("04");
    }
}
