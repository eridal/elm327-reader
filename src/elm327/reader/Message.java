package elm327.reader;

public class Message {

    private final String code;

    Message(String code) {
        this.code = code;
    }

    @Override public String toString() {
        return code;
    }

    public byte[] getBytes() {
        return code.getBytes();
    }

    public boolean hasCode(String code) {
        return code != null && code.startsWith(this.code);
    }
}
